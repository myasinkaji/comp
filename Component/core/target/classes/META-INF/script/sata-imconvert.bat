@echo off
set PATH=%PATH%;%IM_HOME%

"%IM_HOME%\convert.exe" "%1" -interlace line -alpha remove -strip -density 72 -quality 90 -thumbnail "100X100>" "%2"
"%IM_HOME%\convert.exe" "%1" -interlace line -alpha remove -strip -density 72 -quality 90 -thumbnail "180X180>" "%3"
"%IM_HOME%\convert.exe" "%1" -interlace line -alpha remove -strip -density 72 -quality 85 -resize "360X360>" "%4"
"%IM_HOME%\convert.exe" "%1" -interlace line -alpha remove -strip -density 72 -quality 85 -resize "800X800>" "%5"
rem -gravity south -background black -splice 0x21 -font Arial -fill white -dither Riemersma -pointsize 14 -draw "gravity southwest text 5,2'SATA.IR / %CREATOR%'" "%5"