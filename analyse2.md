# Goede logs
```
2020-11-17 15:03:17.334 [scala-execution-context-global-8] info.maaskant.jukebox.rfid.Mfrc522CardReader.$anonfun$read$1() TRACE: 1: Reading from RFID
2020-11-17 15:03:17.338 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x12, 0x0)
2020-11-17 15:03:17.339 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x13, 0x0)
2020-11-17 15:03:17.340 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x24, 0x26)
2020-11-17 15:03:17.354 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xe): 0xa0
2020-11-17 15:03:17.355 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xe, 0x20)
2020-11-17 15:03:17.357 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.358 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x4, 0x7f)
2020-11-17 15:03:17.359 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.362 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x26, 1 bytes)
2020-11-17 15:03:17.363 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x7)
2020-11-17 15:03:17.364 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0xc)
2020-11-17 15:03:17.365 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xd): 0x7
2020-11-17 15:03:17.366 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x87)
2020-11-17 15:03:17.367 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:03:17.368 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x64
2020-11-17 15:03:17.369 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x6): 0x0
2020-11-17 15:03:17.370 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xa): 0x2
2020-11-17 15:03:17.371 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: FIFO Level: 2
2020-11-17 15:03:17.372 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x9, 2 bytes, 0): 0x4400
2020-11-17 15:03:17.373 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xc): 0x10
2020-11-17 15:03:17.373 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: ok
2020-11-17 15:03:17.377 [scala-execution-context-global-8] info.maaskant.jukebox.rfid.Mfrc522CardReader.$anonfun$read$2() TRACE: 2: Card present; reading serial
2020-11-17 15:03:17.378 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xe): 0x20
2020-11-17 15:03:17.378 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xe, 0x20)
2020-11-17 15:03:17.379 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: uid_complete: false, cascade_level: 1
2020-11-17 15:03:17.380 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: select_done: false
2020-11-17 15:03:17.381 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: ANTICOLLISION: current_level_known_bits=0
2020-11-17 15:03:17.382 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.383 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: tx_last_bits: 0
2020-11-17 15:03:17.384 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.385 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x4, 0x7f)
2020-11-17 15:03:17.385 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.386 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x9320, 2 bytes)
2020-11-17 15:03:17.387 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.387 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0xc)
2020-11-17 15:03:17.388 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xd): 0x0
2020-11-17 15:03:17.389 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x80)
2020-11-17 15:03:17.390 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:03:17.390 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x64
2020-11-17 15:03:17.391 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x6): 0x0
2020-11-17 15:03:17.392 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xa): 0x5
2020-11-17 15:03:17.392 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: FIFO Level: 5
2020-11-17 15:03:17.393 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x9, 5 bytes, 0): 0x880446BB71
2020-11-17 15:03:17.394 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xc): 0x10
2020-11-17 15:03:17.395 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: ok
2020-11-17 15:03:17.395 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: select_done: false
2020-11-17 15:03:17.396 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: SELECT: current_level_known_bits=32
2020-11-17 15:03:17.397 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.398 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x5, 0x4)
2020-11-17 15:03:17.399 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.400 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x9370880446BB710000, 7 bytes)
2020-11-17 15:03:17.401 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x3)
2020-11-17 15:03:17.402 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x5): 0x4
2020-11-17 15:03:17.403 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.404 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x22): 0x62
2020-11-17 15:03:17.405 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x21): 0x17
2020-11-17 15:03:17.405 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.406 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: tx_last_bits: 0
2020-11-17 15:03:17.407 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.407 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x4, 0x7f)
2020-11-17 15:03:17.408 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.409 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x9370880446BB716217, 9 bytes)
2020-11-17 15:03:17.410 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.411 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0xc)
2020-11-17 15:03:17.411 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xd): 0x0
2020-11-17 15:03:17.412 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x80)
2020-11-17 15:03:17.413 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:03:17.414 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x44
2020-11-17 15:03:17.414 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x64
2020-11-17 15:03:17.415 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x6): 0x0
2020-11-17 15:03:17.416 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xa): 0x3
2020-11-17 15:03:17.416 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: FIFO Level: 3
2020-11-17 15:03:17.417 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x9, 3 bytes, 0): 0x04DA17
2020-11-17 15:03:17.418 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xc): 0x10
2020-11-17 15:03:17.418 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: ok
2020-11-17 15:03:17.419 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.420 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x5, 0x4)
2020-11-17 15:03:17.421 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.422 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x04DA17, 1 bytes)
2020-11-17 15:03:17.422 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x3)
2020-11-17 15:03:17.423 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x5): 0x4
2020-11-17 15:03:17.424 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.425 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x22): 0xda
2020-11-17 15:03:17.425 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x21): 0x17
2020-11-17 15:03:17.426 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: uid_complete: false, cascade_level: 2
2020-11-17 15:03:17.427 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: select_done: false
2020-11-17 15:03:17.428 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: ANTICOLLISION: current_level_known_bits=0
2020-11-17 15:03:17.428 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.429 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: tx_last_bits: 0
2020-11-17 15:03:17.430 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.431 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x4, 0x7f)
2020-11-17 15:03:17.432 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.433 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x9520, 2 bytes)
2020-11-17 15:03:17.434 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.435 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0xc)
2020-11-17 15:03:17.436 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xd): 0x0
2020-11-17 15:03:17.436 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x80)
2020-11-17 15:03:17.437 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:03:17.438 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x64
2020-11-17 15:03:17.438 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x6): 0x0
2020-11-17 15:03:17.439 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xa): 0x5
2020-11-17 15:03:17.440 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: FIFO Level: 5
2020-11-17 15:03:17.441 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x9, 5 bytes, 0): 0x4A325E81A7
2020-11-17 15:03:17.442 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xc): 0x10
2020-11-17 15:03:17.442 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: ok
2020-11-17 15:03:17.443 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: select_done: false
2020-11-17 15:03:17.443 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: SELECT: current_level_known_bits=32
2020-11-17 15:03:17.444 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.445 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x5, 0x4)
2020-11-17 15:03:17.446 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.447 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x95704A325E81A7DA17, 7 bytes)
2020-11-17 15:03:17.448 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x3)
2020-11-17 15:03:17.449 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x5): 0x4
2020-11-17 15:03:17.450 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.451 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x22): 0x6
2020-11-17 15:03:17.452 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x21): 0xae
2020-11-17 15:03:17.452 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.453 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: tx_last_bits: 0
2020-11-17 15:03:17.454 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.455 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x4, 0x7f)
2020-11-17 15:03:17.456 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.457 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x95704A325E81A706AE, 9 bytes)
2020-11-17 15:03:17.458 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.459 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0xc)
2020-11-17 15:03:17.460 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xd): 0x0
2020-11-17 15:03:17.461 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x80)
2020-11-17 15:03:17.462 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:03:17.463 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x44
2020-11-17 15:03:17.464 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x64
2020-11-17 15:03:17.465 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x6): 0x0
2020-11-17 15:03:17.466 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xa): 0x3
2020-11-17 15:03:17.467 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: FIFO Level: 3
2020-11-17 15:03:17.468 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x9, 3 bytes, 0): 0x00FE51
2020-11-17 15:03:17.469 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xc): 0x10
2020-11-17 15:03:17.470 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: ok
2020-11-17 15:03:17.471 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.472 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x5, 0x4)
2020-11-17 15:03:17.473 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.473 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x00FE51, 1 bytes)
2020-11-17 15:03:17.474 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x3)
2020-11-17 15:03:17.475 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x5): 0x4
2020-11-17 15:03:17.475 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.476 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x22): 0xfe
2020-11-17 15:03:17.477 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x21): 0x51
2020-11-17 15:03:17.479 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.select() DEBUG: End of while (!uidComplete) loop, uid: UID [uidBytes=0446BB4A325E81, sak=0]
2020-11-17 15:03:17.530 [scala-execution-context-global-8] info.maaskant.jukebox.rfid.Mfrc522CardReader.$anonfun$read$2() TRACE: 3: Halting
2020-11-17 15:03:17.531 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.532 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x5, 0x4)
2020-11-17 15:03:17.532 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.533 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x50000000, 4 bytes)
2020-11-17 15:03:17.533 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x3)
2020-11-17 15:03:17.534 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x5): 0x4
2020-11-17 15:03:17.534 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.535 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x22): 0x16
2020-11-17 15:03:17.536 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x21): 0x83
2020-11-17 15:03:17.536 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:03:17.537 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x4, 0x7f)
2020-11-17 15:03:17.538 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:03:17.538 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x50001683, 4 bytes)
2020-11-17 15:03:17.539 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x0)
2020-11-17 15:03:17.540 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0xc)
2020-11-17 15:03:17.541 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xd): 0x0
2020-11-17 15:03:17.541 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x80)
2020-11-17 15:03:17.542 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:03:17.543 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x64
2020-11-17 15:03:17.543 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x6): 0x0
2020-11-17 15:03:17.544 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xa): 0x1
2020-11-17 15:03:17.545 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: FIFO Level: 1
2020-11-17 15:03:17.545 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x9, 1 bytes, 0): 0x01
2020-11-17 15:03:17.546 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xc): 0x14
2020-11-17 15:03:17.546 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: ok
2020-11-17 15:03:17.547 [scala-execution-context-global-8] info.maaskant.jukebox.rfid.Mfrc522CardReader.$anonfun$read$2() TRACE: E1b: Error while halting
2020-11-17 15:03:18.578 [scala-execution-context-global-8] info.maaskant.jukebox.Application.$anonfun$pipeline$2() DEBUG: Physical card: Some(Card(Uid(0446bb4a325e81), MIFARE Ultralight or Ultralight C))
```

# Slechte logs
```
2020-11-17 15:07:09.941 [scala-execution-context-global-8] info.maaskant.jukebox.rfid.Mfrc522CardReader.$anonfun$read$1() TRACE: 1: Reading from RFID
2020-11-17 15:07:09.945 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x12, 0x0)
2020-11-17 15:07:09.946 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x13, 0x0)
2020-11-17 15:07:09.947 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x24, 0x26)
2020-11-17 15:07:09.961 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xe): 0xa0
2020-11-17 15:07:09.962 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xe, 0x20)
2020-11-17 15:07:09.963 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0x0)
2020-11-17 15:07:09.963 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x4, 0x7f)
2020-11-17 15:07:09.964 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xa, 0x80)
2020-11-17 15:07:09.967 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x9, 0x26, 1 bytes)
2020-11-17 15:07:09.968 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x7)
2020-11-17 15:07:09.969 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0x1, 0xc)
2020-11-17 15:07:09.970 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0xd): 0x7
2020-11-17 15:07:09.970 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.writeRegister() DEBUG: (0xd, 0x87)
2020-11-17 15:07:09.971 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.972 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.973 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.974 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.975 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.976 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.977 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.977 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.978 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.979 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.979 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.980 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.981 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.982 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.983 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.984 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.984 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.985 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.986 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.987 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.987 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.988 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.989 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.990 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.991 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.992 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.993 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.993 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.994 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.995 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.996 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.996 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.997 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.998 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:09.999 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.000 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.001 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.002 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.003 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.004 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.005 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.005 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.006 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.007 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.007 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.008 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.009 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.010 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.010 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.011 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.012 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.013 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.014 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.015 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.016 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.017 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.017 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.018 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.018 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.019 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.020 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.021 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.021 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.022 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.023 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.023 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.024 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.025 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.025 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.026 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.027 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.027 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.028 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.029 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.029 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.030 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.030 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.031 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.032 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.032 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.033 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.034 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.035 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.036 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.037 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.037 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.038 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.039 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.039 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.040 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.040 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.041 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.042 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.042 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.043 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.044 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.045 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.046 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.047 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.048 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.048 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.049 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.050 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.051 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.052 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.053 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.054 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.055 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.055 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.056 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.057 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.058 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.059 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.060 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.061 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.062 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.063 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.064 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.065 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.066 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.066 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.067 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.068 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.068 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.069 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.069 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.070 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.071 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.071 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.072 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.072 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.073 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.073 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.074 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.075 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.075 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.076 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.076 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.077 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.077 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
...
2020-11-17 15:07:10.171 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.readRegister() DEBUG: (0x4): 0x4
2020-11-17 15:07:10.171 [scala-execution-context-global-8] com.wiozero.devices.MFRC522.communicateWithPICC() DEBUG: Timed out waiting for interrupt
2020-11-17 15:07:10.175 [scala-execution-context-global-8] info.maaskant.jukebox.rfid.Mfrc522CardReader.$anonfun$read$2() TRACE: E3: No card present
2020-11-17 15:07:11.187 [scala-execution-context-global-8] info.maaskant.jukebox.Application.$anonfun$pipeline$2() DEBUG: Physical card: None  
