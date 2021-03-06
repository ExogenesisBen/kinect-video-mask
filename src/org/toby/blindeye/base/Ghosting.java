package org.toby.blindeye.base;

import KinectPV2.KinectPV2;
import org.toby.blindeye.interfaces.LoadersInterface;
import processing.core.PImage;

import java.util.Random;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.toby.blindeye.UtilitiesAndConstants.BLACK;

class Ghosting implements LoadersInterface {

  private Random rand;

  Ghosting() {
    rand = new Random();
  }

  public PImage execute(PImage liveVideo, PImage body, PImage savedBackground, KinectPV2 kinectPV2) {
    liveVideo.loadPixels();
    int bodyUpscaledSize = (body.width * body.height);
    boolean wasWhiteLR = false;
    for (int i = 0; i < bodyUpscaledSize; i++) {
      if (body.pixels[i] == BLACK) {
        liveVideo.pixels[i] = savedBackground.pixels[i]; // standard ghosting
        if (wasWhiteLR) {
          randomnessHorizontal(i, liveVideo, savedBackground);
        }
        wasWhiteLR = false;
      } else {
        if (!wasWhiteLR) {
          randomnessHorizontal(i, liveVideo, savedBackground);
        }
        wasWhiteLR = true;
      }
      if (body.pixels[i] == BLACK && floor(i/1302) < 1078 && body.pixels[i+1304] != BLACK) {
        randomnessVertical(i, liveVideo, savedBackground);
      }
    }
    liveVideo.updatePixels();
    return liveVideo;
  }

  private void randomnessHorizontal(int i, PImage liveVideo, PImage savedBackground) {
    int randomDistance = rand.nextInt(10) + 30; // line length is 30 to 40 pixels
    for (int j = -randomDistance/2; j <= randomDistance; j++) {
      int position = i % 1302 + j;
      if ((position >= 0) && (position < 1302)) {
        liveVideo.pixels[i+j] = savedBackground.pixels[i+j];
      }
    }
  }

  private void randomnessVertical(int i, PImage liveVideo, PImage savedBackground) {
    int randomDistance = rand.nextInt(20) + 50; // line length is 50 to 70 pixels
    for (int j = 0; j <= randomDistance; j++) {
      if ((floor(i / 1302) + j >= 0) && (ceil(i/1302) + j < 1080)) {
        liveVideo.pixels[i+(j*1302)] = savedBackground.pixels[i+(j*1302)];
      }
    }
  }

}
