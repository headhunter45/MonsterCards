package com.majinnaibu.monstercards.ui.compendium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.CompendiumSourceManager;
import com.majinnaibu.monstercards.models.ImportSource;
import com.majinnaibu.monstercards.ui.components.SourceTagView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompendiumSourcesAdapter extends RecyclerView.Adapter<CompendiumSourcesAdapter.ViewHolder> {

    public interface SourceActionListener {
        void onDownloadClicked(@NonNull ImportSource source);
        void onRemoveClicked(@NonNull ImportSource source);
    }

    public static class SourceDownloadState {
        public boolean isDownloading;
        public boolean hasUpdate;
        public int progress;
        public int max;
        public String statusText;

        public SourceDownloadState(boolean isDownloading, boolean hasUpdate, int progress, int max, String statusText) {
            this.isDownloading = isDownloading;
            this.hasUpdate = hasUpdate;
            this.progress = progress;
            this.max = max;
            this.statusText = statusText;
        }
    }

    private final Context mContext;
    private final List<ImportSource> mSources;
    private final SourceActionListener mListener;
    private final Map<String, SourceDownloadState> mStates = new HashMap<>();

    public CompendiumSourcesAdapter(@NonNull Context context, @NonNull List<ImportSource> sources, @NonNull SourceActionListener listener) {
        mContext = context;
        mSources = sources;
        mListener = listener;
    }

    public void updateProgress(@NonNull String sourceId, boolean isDownloading, int progress, int max, String statusText) {
        SourceDownloadState existing = mStates.get(sourceId);
        boolean hasUpdate = existing != null && existing.hasUpdate;
        mStates.put(sourceId, new SourceDownloadState(isDownloading, hasUpdate, progress, max, statusText));
        notifySourceChanged(sourceId);
    }

    public void setSourceHasUpdate(@NonNull String sourceId, boolean hasUpdate) {
        SourceDownloadState existing = mStates.get(sourceId);
        if (existing != null) {
            existing.hasUpdate = hasUpdate;
        } else {
            mStates.put(sourceId, new SourceDownloadState(false, hasUpdate, 0, 0, null));
        }
        notifySourceChanged(sourceId);
    }

    private void notifySourceChanged(@NonNull String sourceId) {
        for (int i = 0; i < mSources.size(); i++) {
            if (mSources.get(i).id.equals(sourceId)) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_compendium_source, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImportSource source = mSources.get(position);
        holder.textTitle.setText(source.projectName);
        holder.textDescription.setText(source.description);
        holder.textUrl.setText(source.creatorPageLink != null ? source.creatorPageLink : (source.downloadUrl != null ? source.downloadUrl : ""));
        holder.textEstimatedSize.setText(String.format("Size: %s", source.estimatedSize));

        // Source Tag Badge
        holder.sourceTagView.setSource(source.gameSystem, source.sourceLabel);

        boolean isDownloaded = CompendiumSourceManager.isSourceDownloaded(mContext, source.id);
        int count = CompendiumSourceManager.getSourceMonsterCount(mContext, source.id);
        SourceDownloadState state = mStates.get(source.id);
        boolean hasUpdate = state != null && state.hasUpdate;

        if (state != null && state.isDownloading) {
            holder.layoutProgress.setVisibility(View.VISIBLE);
            holder.textProgressStatus.setText(state.statusText != null ? state.statusText : mContext.getString(R.string.status_downloading));
            if (state.max > 0) {
                holder.progressBar.setIndeterminate(false);
                holder.progressBar.setMax(state.max);
                holder.progressBar.setProgress(state.progress);
            } else {
                holder.progressBar.setIndeterminate(true);
            }
            holder.buttonDownload.setEnabled(false);
            holder.buttonDownload.setText(R.string.status_downloading);
            holder.buttonRemove.setVisibility(View.GONE);
            holder.textStatus.setText(R.string.status_downloading);
        } else {
            holder.layoutProgress.setVisibility(View.GONE);
            holder.buttonDownload.setEnabled(true);
            if (isDownloaded) {
                if (hasUpdate) {
                    holder.textStatus.setText("Update Available! (" + count + " current)");
                    holder.buttonDownload.setText("Update Compendium");
                } else {
                    holder.textStatus.setText(mContext.getString(R.string.status_downloaded, count));
                    holder.buttonDownload.setText(R.string.action_redownload);
                }
                holder.buttonRemove.setVisibility(View.VISIBLE);
            } else {
                holder.textStatus.setText(R.string.status_not_downloaded);
                holder.buttonDownload.setText(R.string.action_download);
                holder.buttonRemove.setVisibility(View.GONE);
            }
        }

        holder.buttonDownload.setOnClickListener(v -> mListener.onDownloadClicked(source));
        holder.buttonRemove.setOnClickListener(v -> mListener.onRemoveClicked(source));
    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTitle;
        final TextView textDescription;
        final TextView textUrl;
        final TextView textStatus;
        final TextView textEstimatedSize;
        final SourceTagView sourceTagView;
        final View layoutProgress;
        final LinearProgressIndicator progressBar;
        final TextView textProgressStatus;
        final MaterialButton buttonDownload;
        final MaterialButton buttonRemove;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_source_title);
            textDescription = itemView.findViewById(R.id.text_source_description);
            textUrl = itemView.findViewById(R.id.text_source_url);
            textStatus = itemView.findViewById(R.id.text_source_status);
            textEstimatedSize = itemView.findViewById(R.id.text_estimated_size);
            sourceTagView = itemView.findViewById(R.id.source_tag_view);
            layoutProgress = itemView.findViewById(R.id.layout_progress);
            progressBar = itemView.findViewById(R.id.progress_bar);
            textProgressStatus = itemView.findViewById(R.id.text_progress_status);
            buttonDownload = itemView.findViewById(R.id.button_download);
            buttonRemove = itemView.findViewById(R.id.button_remove);
        }
    }
}
