# Python Tools for the RFID Jukebox

## Documentation

https://pimylifeup.com/raspberry-pi-rfid-rc522/


## Python Environment

Installing Pipenv if you have not done so:
```bash
sudo apt purge python-pip
sudo apt install python3-pip
pip3 install --user pipenv
```

Creating a venv if you have not done so:
```bash
pipenv --three
```

Activating and using the venv:
```bash
PATH=~/.local/bin:$PATH
pipenv shell
pipenv update
```


## Synchronizing to Raspberry Pi

```
rsync -aPv ~/dev/music-album-loader/ pi@framboos:/tmp/music-album-loader --exclude '.*' --exclude Pipfile.lock
```
