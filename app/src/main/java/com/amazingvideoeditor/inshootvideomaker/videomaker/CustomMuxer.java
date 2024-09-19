package com.amazingvideoeditor.inshootvideomaker.videomaker;

import androidx.annotation.NonNull;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.transformer.Muxer;

import com.google.common.collect.ImmutableList;

import java.io.FileDescriptor;
import java.nio.ByteBuffer;

@UnstableApi
public final class CustomMuxer implements Muxer {

    private final Muxer muxer;

    private CustomMuxer(Muxer muxer) {
        this.muxer = muxer;
    }

    @Override
    public int addTrack(@NonNull Format format) throws MuxerException {
        return muxer.addTrack(format);
    }

    @Override
    public void writeSampleData(
            int trackIndex, @NonNull ByteBuffer data, long presentationTimeUs, @C.BufferFlags int flags)
            throws MuxerException {
        muxer.writeSampleData(trackIndex, data, presentationTimeUs, flags);
    }

    @Override
    public void addMetadata(@NonNull Metadata metadata) {
        muxer.addMetadata(metadata);
    }

    @Override
    public void release(boolean forCancellation) throws MuxerException {
        muxer.release(forCancellation);
    }

    @Override
    public long getMaxDelayBetweenSamplesMs() {
        return muxer.getMaxDelayBetweenSamplesMs();
    }

    public static final class Factory implements Muxer.Factory {

        public static final long DEFAULT_MAX_DELAY_BETWEEN_SAMPLES_MS = 10_000;

        private final Muxer.Factory muxerFactory;

        public Factory() {
            this(null, DEFAULT_MAX_DELAY_BETWEEN_SAMPLES_MS);
        }

        public Factory(FileDescriptor fd) {
            this(fd, DEFAULT_MAX_DELAY_BETWEEN_SAMPLES_MS);
        }


        public Factory(FileDescriptor fd, long maxDelayBetweenSamplesMs) {
            this(fd, maxDelayBetweenSamplesMs, /* videoDurationMs= */ C.TIME_UNSET);
        }


        public Factory(FileDescriptor fd, long maxDelayBetweenSamplesMs, long videoDurationMs) {
            this.muxerFactory = new CustomFrameworkMuxer.Factory(fd, maxDelayBetweenSamplesMs, videoDurationMs);
        }

        @NonNull
        @Override
        public Muxer create(@NonNull String path) throws MuxerException {
            return new CustomMuxer(muxerFactory.create(path));
        }

        @NonNull
        @Override
        public ImmutableList<String> getSupportedSampleMimeTypes(@C.TrackType int trackType) {
            return muxerFactory.getSupportedSampleMimeTypes(trackType);
        }
    }
}
