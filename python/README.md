# Python Tools for the RFID Jukebox

## Documentation

https://pimylifeup.com/raspberry-pi-rfid-rc522/


## Python Environment

Installing Pipenv if you have not done so:
```sh
sudo apt purge python-pip
sudo apt install python3-pip
pip3 install --user pipenv
```

Installing compilation dependencies:
```sh
sudo apt-get install libc6-dev python3-dev
```

Activating and using the venv:
```sh
PATH=~/.local/bin:$PATH
pipenv shell
pipenv update
```


## Synchronizing to Raspberry Pi

```
rsync -aPv ~/dev/music-album-loader/ pi@framboos:/tmp/music-album-loader --exclude '.*' --exclude Pipfile.lock
```
