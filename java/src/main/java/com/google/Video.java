package com.google;

import java.util.Collections;
import java.util.List;

/**
 * A class used to represent a video.
 * I modified it to facilitate the completion of tasks; I've overridden
 * compareTo and toString.
 * @author Koichi Ueno
 * @version 21.06.30
 */
class Video implements Comparable<Video>{

  private final String title;
  private final String videoId;
  private final List<String> tags;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  @Override
  public String toString(){
    String video = title + " (" + videoId + ") " +"[";
    if(!tags.isEmpty()) {
      for (String tag : tags) {
        video += tag + " ";
      }
      video = video.substring(0, video.length() - 1);
    }
    return video + "]";
  }

  @Override
  public int compareTo(Video video) {
    return this.title.compareTo(video.getTitle());
  }
}
