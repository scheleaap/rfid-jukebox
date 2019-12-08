# Jukebox

## Documentation

https://pimylifeup.com/raspberry-pi-rfid-rc522/

## Installation on Ubuntu 18.04

```bash
# Install pip3 and pipenv
sudo apt purge python-pip
sudo apt install python3-pip
pip3 install --user pipenv
PATH=~/.local/bin:$PATH

# Create venv and activate
pipenv --three
pipenv shell

# Install project dependencies
pipenv update
```
