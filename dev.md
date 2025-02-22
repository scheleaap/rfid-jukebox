## Development

The project uses modified [DIOZero](https://github.com/mattjlewis/diozero) code for reading RFID cards.
The code is based on [this example](https://github.com/mattjlewis/diozero/blob/master/diozero-sampleapps/src/main/java/com/diozero/sampleapps/mfrc522/ReadUid.java).

Synchronizing the code to a Raspberry Pi for development purposes:
```
echo 'version := "0.0.1-SNAPSHOT"' > version.sbt && sbt "Debian / packageBin" && rsync -aPv ~/dev/rfid-jukebox/target/rfid-jukebox-0.0.1-SNAPSHOT/usr/share/rfid-jukebox/ pi@framboos:~/rfid-jukebox-tmp
```

Note: Java 11 won't work on a Raspberry Pi Zero.

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
