Normaal begin:
```
2020-10-31 14:23:17.888 DEBUG i.m.jukebox.rfid.Mfrc522CardReader$ - Opening RFID reader
2020-10-31 14:23:17.986 DEBUG org.pmw.tinylog.Logger$ - Using native device factory class [SysFsDeviceFactory]
2020-10-31 14:23:18.127 DEBUG org.pmw.tinylog.Logger$ - Loaded library '[diozero-system-utils]' from classpath
2020-10-31 14:23:18.129 DEBUG org.pmw.tinylog.Logger$ - Opening [/dev/spidev0.0, 1000000, MODE_0], frequency {} Hz, mode {}
2020-10-31 14:23:18.141 DEBUG org.pmw.tinylog.Logger$ - reset pin was off
```

Goede logs als er geen kaart op ligt:
```
2020-10-31 14:31:15.517 DEBUG i.m.jukebox.rfid.Mfrc522CardReader$ - Opening RFID reader
2020-10-31 14:31:15.740 DEBUG org.pmw.tinylog.Logger$ - Using native device factory class [SysFsDeviceFactory]
2020-10-31 14:31:15.878 DEBUG org.pmw.tinylog.Logger$ - Loaded library '[diozero-system-utils]' from classpath
2020-10-31 14:31:15.879 DEBUG org.pmw.tinylog.Logger$ - Opening [/dev/spidev0.0, 1000000, MODE_0], frequency {} Hz, mode {}
2020-10-31 14:31:15.892 DEBUG org.pmw.tinylog.Logger$ - reset pin was off
2020-10-31 14:31:16.408 DEBUG org.pmw.tinylog.Logger$ - timer interrupt, n: 0x45
2020-10-31 14:31:16.410 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:31:17.441 INFO  info.maaskant.jukebox.Application$ - Logical card: None
2020-10-31 14:31:17.479 DEBUG org.pmw.tinylog.Logger$ - timer interrupt, n: 0x45
2020-10-31 14:31:17.482 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:31:18.512 DEBUG org.pmw.tinylog.Logger$ - timer interrupt, n: 0x45
2020-10-31 14:31:18.518 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
```

Slechte logs als er geen kaart op ligt:
```
2020-10-31 14:08:50.047 DEBUG i.m.jukebox.rfid.Mfrc522CardReader$ - Opening RFID reader
2020-10-31 14:08:50.219 DEBUG org.pmw.tinylog.Logger$ - Using native device factory class [SysFsDeviceFactory]
2020-10-31 14:08:50.401 DEBUG org.pmw.tinylog.Logger$ - Loaded library '[diozero-system-utils]' from classpath
2020-10-31 14:08:50.402 DEBUG org.pmw.tinylog.Logger$ - Opening [/dev/spidev0.0, 1000000, MODE_0], frequency {} Hz, mode {}
2020-10-31 14:08:50.423 DEBUG org.pmw.tinylog.Logger$ - reset pin was off
2020-10-31 14:08:51.022 DEBUG org.pmw.tinylog.Logger$ - timer interrupt, n: 0x45
2020-10-31 14:08:51.024 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:08:52.047 INFO  info.maaskant.jukebox.Application$ - Logical card: None
# HIER WORDT HET VERSCHILLEND: "timer interrupt, n: 0x45" wordt niet meer gelogd
2020-10-31 14:08:52.252 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:08:53.455 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:08:54.658 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:08:55.860 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:08:57.062 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
2020-10-31 14:08:58.264 DEBUG org.pmw.tinylog.Logger$ - Timed out waiting for interrupt
```