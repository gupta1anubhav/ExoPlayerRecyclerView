package com.example.anubhav.exoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.*;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExoPlayerRecyclerView extends RecyclerView implements ExoPlayer.EventListener{

    int targetPosition;
    private List<VideoPojo> videoInfoList = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView videoSurfaceView;
    private ImageView mCoverImage;
    private Context appContext;
    private ProgressBar progressBar;
    private int playPosition = -1;
    @SuppressLint("UseSparseArrays")
    HashMap<Integer,Long> hashMap = new HashMap<>();
    private boolean addedVideo = false;
    private View rowParent;

    public ExoPlayerRecyclerView(Context context) {
        super(context);
        initialize(context);
    }
    public ExoPlayerRecyclerView(Context context,
                                 AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ExoPlayerRecyclerView(Context context,
                                 AttributeSet attrs,
                                 int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public void setVideoList(List<VideoPojo> videoInfoList) {
        this.videoInfoList = videoInfoList;
    }

    private void removeVideoView(PlayerView videoView) {

        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            addedVideo = false;
        }

    }

    public void playExoPlayerVideo() {
        if(hashMap.size()==0){
            for (int i = 0 ; i < videoInfoList.size(); i++){
                hashMap.put(i,0L);
            }
        }
        int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

        if (endPosition - startPosition > 1) {
            endPosition = startPosition + 1;
        }

        if (startPosition < 0 || endPosition < 0) {
            return;
        }

        if (startPosition != endPosition) {
            int startPositionVideoHeight = getVideoHeight(startPosition);
            int endPositionVideoHeight = getVideoHeight(endPosition);
            targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
        } else {
            targetPosition = startPosition;
        }

        if (targetPosition < 0 || targetPosition == playPosition) {
            return;
        }
        playPosition = targetPosition;
        if (videoSurfaceView == null) {
            return;
        }
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        // get target View targetPosition in RecyclerView
        int at = targetPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

        View child = getChildAt(at);
        if (child == null) {
            return;
        }

        ExoRecyclerViewAdapter.ViewHolder holder
                = (ExoRecyclerViewAdapter.ViewHolder) child.getTag();
        if (holder == null) {
            playPosition = -1;
            return;
        }
        mCoverImage = holder.mCover;
        FrameLayout frameLayout = holder.itemView.findViewById(R.id.video_layout);
        progressBar = holder.progressBar;
        frameLayout.addView(videoSurfaceView);
        addedVideo = true;
        rowParent = holder.itemView;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setPlayer(player);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(appContext,
                Util.getUserAgent(appContext, String.valueOf(R.string.app_name)), defaultBandwidthMeter);
        String uriString = videoInfoList.get(targetPosition).getUrl();
        Log.d("Targetpos",String.valueOf(targetPosition));
        HlsMediaSource hlsMediaSource = new HlsMediaSource(Uri.parse(uriString), dataSourceFactory, getHandler(), new AdaptiveMediaSourceEventListener() {
            @Override
            public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {

            }

            @Override
            public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

            }

            @Override
            public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

            }

            @Override
            public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {

            }

            @Override
            public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {

            }

            @Override
            public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {

            }
        });
        if (uriString != null) {
            Long pos = 0L;
            if(hashMap.get(targetPosition)!=null)
                pos = hashMap.get(targetPosition);
            player.prepare(hlsMediaSource);
            player.seekTo(pos);
            player.setPlayWhenReady(true);
        }


    }

    private int getVideoHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }


    private void initialize(Context context) {
        //initialize hashmap to value 0 for each pos
        if(hashMap.size()==0)
            for (int i = 0 ; i < videoInfoList.size(); i++){
                hashMap.put(i,0L);
            }
        appContext = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;

        screenDefaultHeight = point.y;
        videoSurfaceView = new SimpleExoPlayerView(appContext);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl(
                new DefaultAllocator(true, 16),
                Constants.MIN_BUFFER_DURATION,
                Constants.MAX_BUFFER_DURATION,
                Constants.MIN_PLAYBACK_START_BUFFER,
                Constants.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

        player = ExoPlayerFactory.newSimpleInstance(appContext, trackSelector, loadControl);
        videoSurfaceView.setPlayer(player);

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if(player!=null){
                        Log.e("Position",String.valueOf(player.getCurrentPosition()));
                        hashMap.put(targetPosition,player.getCurrentPosition());
                    }
                    playExoPlayerVideo();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (addedVideo && rowParent != null && rowParent.equals(view)) {
                    removeVideoView(videoSurfaceView);
                    playPosition = -1;
                    videoSurfaceView.setVisibility(INVISIBLE);
                }

            }
        });
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        progressBar.setVisibility(VISIBLE);
                        break;
                    case Player.STATE_ENDED:
                        player.seekTo(0);
                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        videoSurfaceView.setVisibility(VISIBLE);
                        videoSurfaceView.setAlpha(1);
                        mCoverImage.setVisibility(GONE);
                        progressBar.setVisibility(GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    public void onPausePlayer() {
        if (videoSurfaceView != null) {
            hashMap.put(targetPosition,player.getCurrentPosition());
            removeVideoView(videoSurfaceView);
            player.release();
            player = null;
            videoSurfaceView = null;
        }
    }

    public void onRestartPlayer() {
        if (videoSurfaceView == null) {
            playPosition = -1;
            initialize(appContext);
            playExoPlayerVideo();
        }
    }

    public void onRelease() {

        if (player != null) {
            player.release();
            player = null;
        }

        rowParent = null;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}