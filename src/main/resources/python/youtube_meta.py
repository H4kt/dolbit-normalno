import json
import re
from urllib.parse import urlparse, parse_qs
import requests

_original_requests_post = requests.Session.post
def _patched_requests_post(self, url, *args, **kwargs):
    kwargs.pop("proxies", None)
    return _original_requests_post(self, url, *args, **kwargs)
requests.Session.post = _patched_requests_post

import httpx

_original_httpx_post = httpx.post
def _patched_httpx_post(url, *args, **kwargs):
    kwargs.pop("proxies", None)
    return _original_httpx_post(url, *args, **kwargs)
httpx.post = _patched_httpx_post

_original_async_client = httpx.AsyncClient
class PatchedAsyncClient(httpx.AsyncClient):
    async def post(self, url, *args, **kwargs):
        kwargs.pop("proxies", None)
        return await super().post(url, *args, **kwargs)
httpx.AsyncClient = PatchedAsyncClient

from youtubesearchpython import VideosSearch, Playlist

def _extract_playlist_id(url):
    parsed = urlparse(url)
    return parse_qs(parsed.query).get("list", [None])[0]

def extract_video_data(video_obj):
    return {
        "id": video_obj.get("id"),
        "type": "video",
        "title": video_obj.get("title"),
        "channel": (video_obj.get("channel") or {}).get("name"),
        "duration": video_obj.get("duration"),
        "thumbnail": (video_obj.get("thumbnails") or [{}])[0].get("url")
    }

def extract_playlist(playlist_url):
    pl = Playlist(playlist_url)
    while getattr(pl, "hasMoreVideos", False):
        pl.getNextVideos()
    info = getattr(pl, "info", {}) or {}
    entries = [extract_video_data(video) for video in pl.videos]
    return {
        "id": info.get("id") or _extract_playlist_id(playlist_url),
        "type": "playlist",
        "title": info.get("title"),
        "channel": (info.get("channel") or {}).get("name"),
        "thumbnail": (info.get("thumbnails") or [{}])[0].get("url"),
        "entries": entries
    }

def search_youtube(query, limit=10):
    if ("youtube.com" in query) or ("youtu.be" in query):
        if "list=" in query:
            result = extract_playlist(query)
            return json.dumps([result])
        m = re.search(r"(?:v=|youtu\.be/)([a-zA-Z0-9_-]{11})", query)
        if m:
            video_id = m.group(1)
            vs = VideosSearch(f"https://www.youtube.com/watch?v={video_id}", limit=1)
            results = vs.result().get("result", [])
            if not results:
                return json.dumps([])
            return json.dumps([extract_video_data(results[0])])
        return json.dumps([])
    vs = VideosSearch(query, limit=limit)
    results = vs.result().get("result", [])
    data = [extract_video_data(item) for item in results]
    return json.dumps(data)
