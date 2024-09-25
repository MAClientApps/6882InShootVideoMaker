package com.amazingvideoeditor.inshootvideomaker.videomaker;

import android.media.MediaCodec;

import androidx.annotation.NonNull;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.muxer.Muxer;

import com.google.common.collect.ImmutableList;

import java.io.FileDescriptor;
import java.nio.ByteBuffer;

@UnstableApi
public final class CustomMuxer implements Muxer {


    public static final class Factory implements Muxer.Factory {
        private final Muxer.Factory muxerFactory;

        public Factory() {
            this(null, /* videoDurationMs= */ C.TIME_UNSET);
        }

        public Factory(FileDescriptor fd) {
            this(fd, /* videoDurationMs= */ C.TIME_UNSET);
        }


        public Factory(FileDescriptor fd, long videoDurationMs) {
            this.muxerFactory = new CustomFrameworkMuxer.Factory(fd, videoDurationMs);
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

    private final Muxer muxer;

    private CustomMuxer(Muxer muxer) {
        this.muxer = muxer;
    }

    @NonNull
    @Override
    public TrackToken addTrack(@NonNull Format format) throws MuxerException {
        return muxer.addTrack(format);
    }

    @Override
    public void writeSampleData(@NonNull TrackToken trackToken, @NonNull ByteBuffer byteBuffer, @NonNull MediaCodec.BufferInfo bufferInfo)
            throws MuxerException {
        muxer.writeSampleData(trackToken, byteBuffer, bufferInfo);
    }

    @Override
    public void addMetadataEntry(@NonNull Metadata.Entry metadataEntry) {
        muxer.addMetadataEntry(metadataEntry);
    }

    @Override
    public void close() throws MuxerException {
        muxer.close();
    }
}
