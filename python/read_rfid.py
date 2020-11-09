#!/usr/bin/env python3
# Source: https://pimylifeup.com/raspberry-pi-rfid-rc522/

import logging
import sys

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522


def main(args):
    logging.basicConfig(
        level=logging.DEBUG,
        format='%(asctime)s %(levelname)s %(message)s [%(module)s.%(funcName)s()]',
    )

    try:
        logging.debug("Initializing")
        reader = SimpleMFRC522()
        try:
            while True:
                logging.debug("Reading")
                id, text = reader.read()
                print(f"id: {id}, text: {text}")
        finally:
            GPIO.cleanup()
    except EnvironmentError as e:
        logging.exception(e)
    except Exception as e:
        logging.exception(e)


if __name__ == '__main__':
    main(sys.argv[1:])
