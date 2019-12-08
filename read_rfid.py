#!/usr/bin/env python3
# Source: https://pimylifeup.com/raspberry-pi-rfid-rc522/

import logging
import sys

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522


def main(args):
    # # Parse command-line arguments.
    # args = argparse.ArgumentParser(
    #     description=__doc__,
    #     formatter_class=argparse.RawDescriptionHelpFormatter
    # )
    # args.add_argument('source', help='The WMSNotes 3 .nf3 file to process.')
    # args.add_argument('target', help='The target directory.')
    # args = args.parse_args()

    # Initialize logging.
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s %(levelname)s %(message)s [%(module)s.%(funcName)s()]',
    )

    try:
        reader = SimpleMFRC522()
        try:
            id, text = reader.read()
            print(id)
            print(text)
        finally:
            GPIO.cleanup()
    except EnvironmentError as e:
        logging.exception(e)
    except Exception as e:
        logging.exception(e)


if __name__ == '__main__':
    main(sys.argv[1:])
