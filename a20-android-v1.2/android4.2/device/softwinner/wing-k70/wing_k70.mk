# wing fpga product config

$(call inherit-product, device/softwinner/wing-common/ProductCommon.mk)

DEVICE_PACKAGE_OVERLAYS := device/softwinner/wing-k70/overlay

# for recovery
PRODUCT_COPY_FILES += \
	device/softwinner/wing-k70/modules/modules/nand.ko:root/nand.ko \
	device/softwinner/wing-k70/modules/modules/gslX680.ko:root/touch.ko \
	device/softwinner/wing-k70/modules/modules/disp.ko:root/disp.ko \
	device/softwinner/wing-k70/modules/modules/lcd.ko:root/lcd.ko \
	device/softwinner/wing-k70/modules/modules/hdmi.ko:root/hdmi.ko \

PRODUCT_COPY_FILES += \
	device/softwinner/wing-k70/kernel:kernel \
	device/softwinner/wing-k70/recovery.fstab:recovery.fstab \

PRODUCT_COPY_FILES += \
	device/softwinner/wing-k70/ueventd.sun7i.rc:root/ueventd.sun7i.rc \
	device/softwinner/wing-k70/init.sun7i.rc:root/init.sun7i.rc \
	device/softwinner/wing-k70/init.sun7i.usb.rc:root/init.sun7i.usb.rc \
	device/softwinner/wing-k70/camera.cfg:system/etc/camera.cfg \
	device/softwinner/wing-k70/media_profiles.xml:system/etc/media_profiles.xml \
	frameworks/native/data/etc/android.hardware.camera.xml:system/etc/permissions/android.hardware.camera.xml	\
	frameworks/native/data/etc/android.hardware.camera.front.xml:system/etc/permissions/android.hardware.camera.front.xml \
	frameworks/native/data/etc/android.hardware.camera.autofocus.xml:system/etc/permissions/android.hardware.camera.autofocus.xml \
	frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml	\

#input device config
PRODUCT_COPY_FILES += \
	device/softwinner/wing-k70/sw-keyboard.kl:system/usr/keylayout/sw-keyboard.kl \
	device/softwinner/wing-k70/tp.idc:system/usr/idc/tp.idc \
	device/softwinner/wing-k70/gsensor.cfg:system/usr/gsensor.cfg

PRODUCT_COPY_FILES += \
	device/softwinner/wing-k70/initlogo.rle:root/initlogo.rle

PRODUCT_COPY_FILES += \
	device/softwinner/wing-k70/vold.fstab:system/etc/vold.fstab

# wifi & bt config file
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
    frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml \
#    frameworks/native/data/etc/android.hardware.bluetooth.xml:system/etc/permissions/android.hardware.bluetooth.xml \
#    system/bluetooth/data/main.nonsmartphone.conf:system/etc/bluetooth/main.conf

# rtl8723as bt fw and config
#PRODUCT_COPY_FILES += \
#	device/softwinner/wing-common/hardware/realtek/bluetooth/rtl8723as/rlt8723a_chip_b_cut_bt40_fw.bin:system/etc/rlt8723a_chip_b_cut_bt40_fw.bin \
#	device/softwinner/wing-common/hardware/realtek/bluetooth/rtl8723as/rtk8723_bt_config:system/etc/rtk8723_bt_config

# bcm40181 sdio wifi fw and nvram
#PRODUCT_COPY_FILES += \
#	hardware/broadcom/wlan/firmware/bcm40181/fw_bcm40181a2_p2p.bin:system/vendor/modules/fw_bcm40181a2_p2p.bin \
#	hardware/broadcom/wlan/firmware/bcm40181/fw_bcm40181a2_apsta.bin:system/vendor/modules/fw_bcm40181a2_apsta.bin \
#	hardware/broadcom/wlan/firmware/bcm40181/fw_bcm40181a2.bin:system/vendor/modules/fw_bcm40181a2.bin \
#	hardware/broadcom/wlan/firmware/bcm40181/bcm40181_nvram.txt:system/vendor/modules/bcm40181_nvram.txt

# bcm40183 sdio wifi fw and nvram
#PRODUCT_COPY_FILES += \
#	hardware/broadcom/wlan/firmware/bcm40183/fw_bcm40183b2_p2p.bin:system/vendor/modules/fw_bcm40183b2_p2p.bin \
#	hardware/broadcom/wlan/firmware/bcm40183/fw_bcm40183b2_apsta.bin:system/vendor/modules/fw_bcm40183b2_apsta.bin \
#	hardware/broadcom/wlan/firmware/bcm40183/fw_bcm40183b2.bin:system/vendor/modules/fw_bcm40183b2.bin \
#	hardware/broadcom/wlan/firmware/bcm40183/bcm40183_nvram.txt:system/vendor/modules/bcm40183_nvram.txt

# ap6210 sdio wifi fw and nvram
PRODUCT_COPY_FILES += \
	hardware/broadcom/wlan/firmware/ap6210/fw_bcm40181a2.bin:system/vendor/modules/fw_bcm40181a2.bin \
	hardware/broadcom/wlan/firmware/ap6210/fw_bcm40181a2_apsta.bin:system/vendor/modules/fw_bcm40181a2_apsta.bin \
	hardware/broadcom/wlan/firmware/ap6210/fw_bcm40181a2_p2p.bin:system/vendor/modules/fw_bcm40181a2_p2p.bin \
	hardware/broadcom/wlan/firmware/ap6210/nvram_ap6210.txt:system/vendor/modules/nvram_ap6210.txt \
	hardware/broadcom/wlan/firmware/ap6210/bcm20710a1.hcd:system/vendor/modules/bcm20710a1.hcd

# copy key.apk for updater install external apk signature key
PRODUCT_COPY_FILES += \
	device/softwinner/wing-k70/key.apk:system/vendor/key.apk	

PRODUCT_PROPERTY_OVERRIDES += \
  dalvik.vm.heapsize=80m \
  persist.sys.usb.config=mass_storage,adb \
	ro.property.tabletUI=true \
	ro.sf.lcd_density=200 \
	ro.udisk.lable=WING \
	ro.product.firmware=v1.2 \
	ro.property.fontScale=1.15 \

$(call inherit-product-if-exists, device/softwinner/wing-k70/modules/modules.mk)

PRODUCT_CHARACTERISTICS := tablet

# Overrides
PRODUCT_BRAND  := softwinners
PRODUCT_NAME   := wing_k70
PRODUCT_DEVICE := wing-k70
PRODUCT_MODEL  := SoftwinerEvb

