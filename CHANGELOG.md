# Changelog

## [1.1.6](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.5...v1.1.6) (2020-11-18)


### Bug Fixes

* Improve RFID reading robustness ([f1f9d6b](https://github.com/scheleaap/rfid-jukebox/commit/f1f9d6b0628cd1fa49407c0a5b09f908deb1a242))

## [1.1.5](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.4...v1.1.5) (2020-10-31)


### Bug Fixes

* Upgrade to diozero 0.13, switch from Logback to Tinylog ([80dc821](https://github.com/scheleaap/rfid-jukebox/commit/80dc821b65683e8f074841946d7272c0bc7e2c14))

## [1.1.4](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.3...v1.1.4) (2020-10-31)


### Bug Fixes

* Make MFRC522 log level configurable ([5a5d51f](https://github.com/scheleaap/rfid-jukebox/commit/5a5d51fddd5df8e724889b90b37712ceff2da493))

## [1.1.3](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.2...v1.1.3) (2020-07-31)


### Bug Fixes

* Send Content-Type header to Mopidy ([f8ce535](https://github.com/scheleaap/rfid-jukebox/commit/f8ce535890aff6bda9ca86fb927af06c5ccdfe52))

## [1.1.2](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.1...v1.1.2) (2020-07-31)


### Bug Fixes

* Add log message when shutting down ([7183037](https://github.com/scheleaap/rfid-jukebox/commit/71830373050ca3c3c9225ff109ffd02c96782b87))
* Log failed calls to Mopidy ([4c89a88](https://github.com/scheleaap/rfid-jukebox/commit/4c89a88215a4caf4ff1b59576dac7ba512e4049c))

## [1.1.1](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.0...v1.1.1) (2020-05-24)


### Bug Fixes

* Shutdown computer with sudo ([043bac8](https://github.com/scheleaap/rfid-jukebox/commit/043bac8064b2dde60e2782e8f07eb7ed7b719a86))

# [1.1.0](https://github.com/scheleaap/rfid-jukebox/compare/v1.0.1...v1.1.0) (2020-05-24)


### Features

* Allow cards to shut down the computer ([a89dbbe](https://github.com/scheleaap/rfid-jukebox/commit/a89dbbe794926129ed8bcf37110646cd9800a38f))

## [1.0.1](https://github.com/scheleaap/rfid-jukebox/compare/v1.0.0...v1.0.1) (2020-03-16)


### Bug Fixes

* Prevent NPE if card UID cannot be read ([e0f5001](https://github.com/scheleaap/rfid-jukebox/commit/e0f50010a76183d7b9e88f450dd28012a0ab826a))
* Remove default RFID tags from config ([c8b4fb4](https://github.com/scheleaap/rfid-jukebox/commit/c8b4fb46832f3323649c6afdce7e2300d9a93e0f))
* Shorten Debian package summary ([5af843e](https://github.com/scheleaap/rfid-jukebox/commit/5af843e095fde73349620f82066efc9ba2cb848f))

## 1.0.0 (2020-02-24)

First version of Scala-based RFID reader.
