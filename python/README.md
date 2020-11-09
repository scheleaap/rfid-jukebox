# Python Tools for the RFID Jukebox

## Documentation

https://pimylifeup.com/raspberry-pi-rfid-rc522/


## Synchronizing to Raspberry Pi

```
rsync -aPv ~/dev/rfid-jukebox/python/ pi@framboos:/tmp/rfid-jukebox --exclude '.*' --exclude Pipfile.lock
```
