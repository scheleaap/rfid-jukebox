# Changelog

## [1.4.3](https://github.com/scheleaap/rfid-jukebox/compare/v1.4.2...v1.4.3) (2025-02-06)


### Bug Fixes

* Add more IO.blocking ([df3b9b2](https://github.com/scheleaap/rfid-jukebox/commit/df3b9b28a345f049d2de58a21d5c997bb97b8db7))
* Revert back to OpenJDK 8 ([ea31504](https://github.com/scheleaap/rfid-jukebox/commit/ea31504dbb3b2a161709d4b64dde60e07b766308))
* Revert back to OpenJDK 8 ([4097242](https://github.com/scheleaap/rfid-jukebox/commit/4097242152e4110a135c4a45e42f1564d9ec4c82))

## [1.4.2](https://github.com/scheleaap/rfid-jukebox/compare/v1.4.1...v1.4.2) (2025-02-06)


### Bug Fixes

* Small changes ([1c289a0](https://github.com/scheleaap/rfid-jukebox/commit/1c289a07daa11543720ee975cba2556b072b17ce))

## [1.4.1](https://github.com/scheleaap/rfid-jukebox/compare/v1.4.0...v1.4.1) (2025-02-06)


### Bug Fixes

* Use IO.blocking to read RFID cards ([69d2f63](https://github.com/scheleaap/rfid-jukebox/commit/69d2f6306ba31ce0629a4091ca72633c6644c66b))

# [1.4.0](https://github.com/scheleaap/rfid-jukebox/compare/v1.3.2...v1.4.0) (2025-02-06)


### Bug Fixes

* Update Pipenv for Python ([5779070](https://github.com/scheleaap/rfid-jukebox/commit/5779070c262c0131d568e90f444209851fce5561))
* Use modified RFID reader by default ([e8fbb03](https://github.com/scheleaap/rfid-jukebox/commit/e8fbb03cf5348a6d9de91308e3fac0210301e1c4))


### Features

* Incremental backoff for reading cards ([#6](https://github.com/scheleaap/rfid-jukebox/issues/6)) ([d228754](https://github.com/scheleaap/rfid-jukebox/commit/d228754a525414f19beeafa60929d455cf6d6126))

## [1.3.2](https://github.com/scheleaap/rfid-jukebox/compare/v1.3.1...v1.3.2) (2021-05-08)


### Bug Fixes

* Revert back to OpenJDK 8 ([c15de04](https://github.com/scheleaap/rfid-jukebox/commit/c15de044db7466e158dbac038f67020b237016c3))

## [1.3.1](https://github.com/scheleaap/rfid-jukebox/compare/v1.3.0...v1.3.1) (2021-05-08)


### Bug Fixes

* Replace dependency on OpenJDK 8 with 11 ([c25bbfe](https://github.com/scheleaap/rfid-jukebox/commit/c25bbfeaf35290f1ed2dc6d3347a75818b292c13))

# [1.3.0](https://github.com/scheleaap/rfid-jukebox/compare/v1.2.2...v1.3.0) (2021-05-02)


### Features

* Restart stream after a pause ([30e778c](https://github.com/scheleaap/rfid-jukebox/commit/30e778c142e056aea2551fa9caabb84a9328b8fc))

## [1.2.2](https://github.com/scheleaap/rfid-jukebox/compare/v1.2.1...v1.2.2) (2021-04-02)


### Bug Fixes

* Improve RFID reading robustness ([be3133d](https://github.com/scheleaap/rfid-jukebox/commit/be3133d98a5d3682f678d92b11a0a5def7bb3e88))
* Provide ability to switch between original and modified MFRC522 reader ([41c0866](https://github.com/scheleaap/rfid-jukebox/commit/41c0866f0006fcb11971e4cb5c622981171e93ff))

## [1.2.1](https://github.com/scheleaap/rfid-jukebox/compare/v1.2.0...v1.2.1) (2021-02-17)


### Bug Fixes

* Distinguish different unknown cards and log their UID ([2ab3801](https://github.com/scheleaap/rfid-jukebox/commit/2ab3801585a2b3786bb6eef3b05264a84f2d9d8c))

# [1.2.0](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.7...v1.2.0) (2020-12-17)


### Features

* Add event hooks ([#1](https://github.com/scheleaap/rfid-jukebox/issues/1)) ([d3a970d](https://github.com/scheleaap/rfid-jukebox/commit/d3a970de55bd5d2bc5b65fa744fae432b973355d))

## [1.1.7](https://github.com/scheleaap/rfid-jukebox/compare/v1.1.6...v1.1.7) (2020-11-18)


### Bug Fixes

* Improve RFID reading robustness ([0ed3329](https://github.com/scheleaap/rfid-jukebox/commit/0ed332948d505c86c6edd2609bffca9ef3d4be91))

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
