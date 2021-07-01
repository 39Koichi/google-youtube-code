package com.google;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A representation of a video player.
 * Implementation provided for the challenge.
 * @author Koichi Ueno
 * @version 2021.06.30
 */
public class VideoPlayer {

  private boolean questionAsked;
  private List<Video> questionChoices;

  /**
   * Accessor method
   */
  public boolean isQuestionAsked() {
    return questionAsked;
  }

  private final VideoLibrary videoLibrary;
  private Video actualVideo;
  private boolean paused;
  private HashMap<String, List<Video>> playlists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    playlists = new HashMap<>();
  }

  /**
   * True if the library contains the video we are looking for.
   * @param videoId The id of the video we are looking for
   * @return true if the library contains it
   */
  private boolean videoExist(String videoId) {
    if(videoLibrary.getVideo(videoId) == null) return false;
    return true;
  }

  /**
   * Prints how many videos are in the library.
   */
  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  /**
   * Prints information about all the videos available in the library,
   * by alphabetical order of video titles.
   */
  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<String> videoInfo = new ArrayList<>();
    List<Video> videos = videoLibrary.getVideos();
    videos.forEach(v -> videoInfo.add(v.toString()));
    Collections.sort(videoInfo);
    videoInfo.forEach(System.out::println);
  }

  /**
   * Plays the specified video. Stops the previous playing video, if exists.
   * An error occurs if the video does not exist.
   * @param videoId The id of the video to play
   */
  public void playVideo(String videoId) {
    if(!videoExist(videoId)){
      System.out.println("Cannot play video: Video does not exist");
      return;
    }
    if(actualVideo != null) System.out.println("Stopping video: " + actualVideo.getTitle());
    actualVideo = videoLibrary.getVideo(videoId);
    System.out.println("Playing video: " + actualVideo.getTitle());
    paused = false;
  }

  /**
   * Stops the current playing video.
   * An error occurs if no video was currently playing.
   */
  public void stopVideo() {
    if(actualVideo != null){
      System.out.println("Stopping video: " + actualVideo.getTitle());
      actualVideo = null;
    }else{
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  /**
   * Plays a random video from the library. Stops the previous playing video
   * if necessary. If no videos are available, it prints out a message.
   */
  public void playRandomVideo() {
    List<Video> videos = new ArrayList<>();
    videoLibrary.getVideos().forEach(videos::add);
    Collections.shuffle(videos);
    if(videos.isEmpty()){
      System.out.println("No videos available");
    }else{
      playVideo(videos.get(0).getVideoId());
    }
  }

  /**
   * Pauses the currently playing video.
   * An error occurs if no video was playing, or if it was already paused.
   */
  public void pauseVideo() {
    if(actualVideo != null){
      if(paused){
        System.out.println("Video already paused: " + actualVideo.getTitle());
        return;
      }
      System.out.println("Pausing video: " + actualVideo.getTitle());
      paused = true;
    }else{
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  /**
   * Continues a currently paused video.
   * An error occurs if no video was paused, or playing.
   */
  public void continueVideo() {
    if(actualVideo == null){
      System.out.println("Cannot continue video: No video is currently playing");
    }else{
      if(!paused){
        System.out.println("Cannot continue video: Video is not paused");
      }else {
        paused = false;
        System.out.println("Continuing video: " + actualVideo.getTitle());
      }
    }
  }

  /**
   * Prints information about the currently playing video.
   * An error occurs if no video is playing.
   */
  public void showPlaying() {
    if(actualVideo == null){
      System.out.println("No video is currently playing");
    }else{
      System.out.print("Currently playing: " + actualVideo);
      if(paused) System.out.print(" - PAUSED");
      System.out.print("\n");
    }
  }

  /**
   * Returns true if there is a playlist with the same name as the
   * parameter, without taking into account letter cases.
   * @param playlistName The name of the playlist to check
   * @return true if there is a playlist with the same name (case insensitive)
   */
  private boolean playlistExist(String playlistName) {
    String lowerCaseName = playlistName.toLowerCase();
    for(String name : playlists.keySet()) {
      if(lowerCaseName.equals(name.toLowerCase())) return true;
    }
    return false;
  }

  /**
   * Returns the real (original input) name of a playlist, giving
   * in parameter its name with different letter cases.
   * Returns null if there is no match.
   * @param playlistName The name of the playlist to check
   * @return the case sensitive name of a playlist
   */
  private String getRealPlaylistName(String playlistName) {
    String lowerCaseName = playlistName.toLowerCase();
    for(String name : playlists.keySet()) {
      if(lowerCaseName.equals(name.toLowerCase())) return name;
    }
    return null;
  }

  /**
   * Returns the playlist from its name (case insensitive).
   * null if the name does not match.
   * @param playlistName The name of the playlist
   * @return the corresponding playlist
   */
  private List<Video> getPlaylist(String playlistName) {
    String realName = getRealPlaylistName(playlistName);
    if(realName != null) return playlists.get(realName);
    return null;
  }

  /**
   * Creates an (empty) playlist giving its name.
   * An error occurs if the name was already used (case insensitive).
   * @param playlistName The name of the playlist to create
   */
  public void createPlaylist(String playlistName) {
    if(!playlistExist(playlistName)) {
      playlists.put(playlistName, new ArrayList<Video>());
      System.out.println("Successfully created new playlist: " + playlistName);
    }else{
      System.out.println("Cannot create playlist: " + "A playlist with the same name already exists");
    }
  }

  /**
   * Adds the specified video to the specified playlist.
   * An error occurs if one of them does not exist, or if the
   * playlist already contains the specified video.
   * @param playlistName The name of the playlist
   * @param videoId The id of the video
   */
  public void addVideoToPlaylist(String playlistName, String videoId) {
    if(!playlistExist(playlistName)){
      System.out.println("Cannot add video to " + playlistName +": Playlist does not exist");
      return;
    }
    if(!videoExist(videoId)){
      System.out.println("Cannot add video to " + playlistName +": Video does not exist");
      return;
    }
    Video videoToAdd = videoLibrary.getVideo(videoId);
    List<Video> playlist = getPlaylist(playlistName);
    if(playlist.contains(videoToAdd)) {
      System.out.println("Cannot add video to " + playlistName +": Video already added");
      return;
    }
    playlist.add(videoToAdd);
    playlists.put(getRealPlaylistName(playlistName), playlist);
    System.out.println("Added video to " + playlistName + ": " + videoToAdd.getTitle());
  }

  /**
   * Prints the name of all the playlists, in lexicographical order.
   * An error occurs if no playlist exists.
   */
  public void showAllPlaylists() {
    if(!playlists.isEmpty()){
      System.out.println("Showing all playlists:");
      playlists.keySet().stream().sorted().forEach(System.out::println);
    }else{
      System.out.println("No playlists exist yet");
    }
  }

  /**
   * Prints information about all the videos in the specified playlist (case insensitive).
   * An error occurs if the playlist does not exist or if it is empty.
   * @param playlistName The specified playlist
   */
  public void showPlaylist(String playlistName) {
    if(playlistExist(playlistName)){
      System.out.println("Showing playlist: " + playlistName);
      List<Video> playlist = getPlaylist(playlistName);
      if(!playlist.isEmpty()){
        playlist.forEach(System.out::println);
      }else{
        System.out.println("No videos here yet");
      }
    }else{
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
  }

  /**
   * Removes the specified video from the specified playlist.
   * An error occurs if either does not exist.
   * @param playlistName The name of the playlist
   * @param videoId The id of the video
   */
  public void removeFromPlaylist(String playlistName, String videoId) {
    if(playlistExist(playlistName)){
      if(videoExist(videoId)){
        List<Video> playlist = getPlaylist(playlistName);
        List<Video> filtered = playlist.stream().filter(v -> !v.getVideoId().equals(videoId)).collect(Collectors.toList());
        if(filtered.size() != playlist.size()){
          System.out.println("Removed video from " + playlistName + ": " + videoLibrary.getVideo(videoId));
          playlists.put(getRealPlaylistName(playlistName), filtered);
        }else{
          System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        }
      }else{
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      }
    }else{
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
  }

  /**
   * Removes all videos from the specified playlist,
   * without deleting the playlist itself.
   * An error occurs if there is no matching playlist
   * @param playlistName The name of the playlist
   */
  public void clearPlaylist(String playlistName) {
    if(playlistExist(playlistName)){
      List<Video> playlist = getPlaylist(playlistName);
      playlist.clear();
      playlists.put(getRealPlaylistName(playlistName), playlist);
      System.out.println("Successfully removed all videos from " + playlistName);
    }else{
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
  }

  /**
   * Deletes the specified playlist.
   * An error occurs if it doesn't exist.
   * @param playlistName The name of the playlist
   */
  public void deletePlaylist(String playlistName) {
    if(playlistExist(playlistName)){
      playlists.remove(getRealPlaylistName(playlistName));
      System.out.println("Deleted playlist: " + playlistName);
    }else{
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
  }

  /**
   * Prints all videos from the library whose title contain the specified
   * search term. The search is case insensitive.
   * @param searchTerm The specified search term
   */
  public void searchVideos(String searchTerm) {
    List<Video> library = videoLibrary.getVideos();
    Iterator<Video> it = library.iterator();
    while(it.hasNext()) {
      if(!it.next().getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
        it.remove();
      }
    }
    Collections.sort(library);
    searchHelper(library, searchTerm);
  }

  /**
   * Method helping searchVideos and searchVideosWithTag.
   * Plays the video corresponding to the index typed by the user, these
   * indexes are different for each distinct search made by a searchVideos call.
   * Nothing happens if the input is not a number, or the given index is wrong.
   * @param input The inputted index
   */
  public void searchVideosQuestion(String input) {
    try {
      //questionAsked = false; // reset, actually not needed
      int index = Integer.parseInt(input);
      if(index - 1 < questionChoices.size()) {
        Video videoToPlay = questionChoices.get(index - 1);
        playVideo(videoToPlay.getVideoId());
      }
    }
    catch(NumberFormatException exc) {
      // nothing to do.
    }
  }

  /**
   * Show all videos whose list of tags contains the specified hashtag.
   * The search is case insensitive.
   * @param videoTag The inputted search hashtag
   */
  public void searchVideosWithTag(String videoTag) {
    List<Video> library = videoLibrary.getVideos();
    Iterator<Video> it = library.iterator();
    while(it.hasNext()) {
      List<String> tags = it.next().getTags();
      if(!tags.contains(videoTag.toLowerCase())) {
        it.remove();
      }
    }
    Collections.sort(library);
    searchHelper(library, videoTag);
  }

  /**
   * Method helping searchVideos and searchVideosWithTag.
   * Prints out all the results of a search, and prepares to play a
   * video giving its index, if possible.
   * @param library The videos to print
   * @param searchWord The search word or tag)
   */
  private void searchHelper(List<Video> library, String searchWord) {
    int count = 0;
    for(Video video : library) {
      if(count == 0) System.out.println("Here are the results for " + searchWord + ":");
      count++;
      System.out.println(count + ") " + video);
    }

    if(count != 0){
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      questionAsked = true;
      questionChoices = library;
    }else{
      System.out.println("No search results for " + searchWord);
      questionAsked = false;
    }
  }

  // I didn't have the time to complete part 4.
  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}