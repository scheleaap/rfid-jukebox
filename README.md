# RFID Jukebox

The jukebox allows you to use RFID tags to control [Mopidy](https://mopidy.com/) and play songs from Spotify (through the [Mopidy-Spotify plugin](https://github.com/mopidy/mopidy-spotify)). It runs on the Raspberry Pi.


## Documentation

https://github.com/mattjlewis/diozero
https://github.com/mattjlewis/diozero/blob/master/diozero-sampleapps/src/main/java/com/diozero/sampleapps/mfrc522/ReadUid.java


## Installation

The jukebox requires Java 8+ to run.

To install Java 11 on Raspberry Pi (not required):
```shell script
# Java
# Source: https://joshefin.xyz/install-java-11-on-a-raspberry-pi/
sudo mkdir /opt/jdk
cd /opt/jdk
VERSION=zulu11.33.21-ca-jdk11.0.4-linux_aarch32hf
sudo wget https://cdn.azul.com/zulu-embedded/bin/${VERSION}.tar.gz \
  && sudo tar -xvzf ${VERSION}.tar.gz \
  && sudo rm *.tar.gz \
  && sudo update-alternatives --install /usr/bin/java java /opt/jdk/${VERSION}/bin/java 1 \
  && sudo update-alternatives --install /usr/bin/javac javac /opt/jdk/${VERSION}/bin/javac 1 \
  && java --version
```
