# full wing product config
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base.mk)
$(call inherit-product, device/softwinner/common/sw-common.mk)

DEVICE_PACKAGE_OVERLAYS := device/softwinner/wing-common/overlay

PRODUCT_PACKAGES += \
	hwcomposer.exDroid \
	display.exdroid \
	camera.exDroid \
	lights.sun7i \
	display.sun7i

PRODUCT_PACKAGES += \
	audio.primary.exDroid \
	audio.a2dp.default \
	libaudioutils \
	libcedarxbase \
	libcedarxosal \
	libcedarv \
	libcedarv_base \
	libcedarv_adapter \
	libve \
	libaw_audio \
	libaw_audioa \
	libswdrm \
	libstagefright_soft_cedar_h264dec \
	libfacedetection \
	libthirdpartstream \
	libcedarxsftstream \
	libaw_h264enc \
	libI420colorconvert.so \
	libstagefrighthw.so \
	libOmxCore.so \
	libOmxVenc.so \
	libOmxVdec.so \
	Camera \
	libjni_mosaic \
	FileExplore \
	u3gmonitor \
	rild \
	chat \
	libjni_pinyinime \
	libsrec_jni \

PRODUCT_PACKAGES += \
	e2fsck \
	libext2fs \
	libext2_blkid \
	libext2_uuid \
	libext2_profile \
	libext2_com_err \
	libext2_e2p \
	make_ext4fs

PRODUCT_PACKAGES += \
	LiveWallpapersPicker \
	LiveWallpapers \
	android.software.live_wallpaper.xml \
	SoundRecorder \
	FileExplore


# keylayout
PRODUCT_COPY_FILES += \
	device/softwinner/wing-common/axp20-supplyer.kl:system/usr/keylayout/axp20-supplyer.kl

# mali lib so
PRODUCT_COPY_FILES += \
	device/softwinner/wing-common/egl/gralloc.sun7i.so:system/lib/hw/gralloc.sun7i.so \
	device/softwinner/wing-common/egl/libMali.so:system/lib/libMali.so \
	device/softwinner/wing-common/egl/libUMP.so:system/lib/libUMP.so \
	device/softwinner/wing-common/egl/egl.cfg:system/lib/egl/egl.cfg \
	device/softwinner/wing-common/egl/libEGL_mali.so:system/lib/egl/libEGL_mali.so \
	device/softwinner/wing-common/egl/libGLESv1_CM_mali.so:system/lib/egl/libGLESv1_CM_mali.so \
	device/softwinner/wing-common/egl/libGLESv2_mali.so:system/lib/egl/libGLESv2_mali.so \

# init.rc, kernel
PRODUCT_COPY_FILES += \
	device/softwinner/wing-common/init.rc:root/init.rc \

# for boot nand/card auto detect 
PRODUCT_COPY_FILES += \
	device/softwinner/wing-common/busybox:root/sbin/busybox \
	device/softwinner/wing-common/init_parttion.sh:root/sbin/init_parttion.sh \
	device/softwinner/wing-common/busybox:recovery/root/sbin/busybox \
	device/softwinner/wing-common/init_parttion.sh:recovery/root/sbin/init_parttion.sh \
	

#premission feature
PRODUCT_COPY_FILES += \
	device/softwinner/wing-common/tablet_core_hardware.xml:system/etc/permissions/tablet_core_hardware.xml \
    frameworks/native/data/etc/android.hardware.location.xml:system/etc/permissions/android.hardware.location.xml \
    frameworks/native/data/etc/android.hardware.sensor.accelerometer.xml:system/etc/permissions/android.hardware.sensor.accelerometer.xml \
    frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.xml:system/etc/permissions/android.hardware.touchscreen.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.multitouch.distinct.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.distinct.xml \


PRODUCT_COPY_FILES += \
	device/softwinner/wing-common/media_codecs.xml:system/etc/media_codecs.xml \
	device/softwinner/wing-common/preinstall.sh:/system/bin/preinstall.sh \
	device/softwinner/wing-common/data_resume.sh:/system/bin/data_resume.sh \
	device/softwinner/wing-common/chrome-command-line:/system/etc/chrome-command-line

PRODUCT_PROPERTY_OVERRIDES += \
	ro.kernel.android.checkjni=0 \
	persist.sys.timezone=Asia/Shanghai \
	persist.sys.language=zh \
	persist.sys.country=CN \
	wifi.interface=wlan0 \
	wifi.supplicant_scan_interval=15 \
	debug.egl.hw=1 \
	ro.display.switch=1 \
	ro.opengles.version=131072 \
	rild.libargs=-d/dev/ttyUSB2 \
	rild.libpath=/system/lib/libsoftwinner-ril.so \
	keyguard.no_require_sim=true \
	persist.sys.strictmode.visual=0 \
	persist.sys.strictmode.disable=1 \
	hwui.render_dirty_regions=false

PRODUCT_PROPERTY_OVERRIDES += \
	drm.service.enabled=true
	
PRODUCT_PACKAGES += \
	com.google.widevine.software.drm.xml \
	com.google.widevine.software.drm \
	libdrmwvmplugin \
	libwvm \
	libWVStreamControlAPI_L3 \
	libwvdrm_L3 \
    libdrmdecrypt	
	
# pre-installed apks
PRODUCT_COPY_FILES += \
	$(call find-copy-subdir-files,*.apk,$(LOCAL_PATH)/preinstallapk,system/preinstall) \
	$(call find-copy-subdir-files,*,$(LOCAL_PATH)/apk,system/app) \
	$(call find-copy-subdir-files,*,$(LOCAL_PATH)/apklib,system/lib) \
	$(call find-copy-subdir-files,*,$(LOCAL_PATH)/apkdata/txt2epub,system/txt2epub) \
  $(call find-copy-subdir-files,*,$(LOCAL_PATH)/googleservice/gapps-jb-20121212-signed/system,system) \

# Overrides
PRODUCT_BRAND  := softwinners
PRODUCT_NAME   := wing_common
PRODUCT_DEVICE := wing-common


