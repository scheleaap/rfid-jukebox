# Album Recognizer

## Documentation

https://www.pyimagesearch.com/2018/05/21/an-opencv-barcode-and-qr-code-scanner-with-zbar/
https://www.pyimagesearch.com/category/building-a-pokedex/

## Installation on Ubuntu 18.04

Sources
* https://www.pyimagesearch.com/2018/08/15/how-to-install-opencv-4-on-ubuntu/)
* https://www.pyimagesearch.com/2018/09/26/install-opencv-4-on-your-raspberry-pi/

```bash
# Install pip3 and pipenv
sudo apt purge python-pip
sudo apt install python3-pip
pip3 install --user pipenv
PATH=~/.local/bin:$PATH

# Create venv and activate
pipenv --three
pipenv shell

# Install OpenCV dependencies
pipenv install numpy

# OpenCV build tools
sudo apt-get install \
  build-essential cmake unzip pkg-config \
  libjpeg-dev libpng-dev libtiff-dev \
  libavcodec-dev libavformat-dev libswscale-dev libv4l-dev \
  libxvidcore-dev libx264-dev \
  libgtk-3-dev \
  libatlas-base-dev gfortran \
  python3-dev

# OpenCV download
export OPENCV_VERSION=4.1.1 \
  && cd ~/tmp \
  && wget -O opencv.zip https://github.com/opencv/opencv/archive/${OPENCV_VERSION}.zip \
  && wget -O opencv_contrib.zip https://github.com/opencv/opencv_contrib/archive/${OPENCV_VERSION}.zip \
  && unzip opencv.zip \
  && unzip opencv_contrib.zip \
  && mv opencv-${OPENCV_VERSION} opencv \
  && mv opencv_contrib-${OPENCV_VERSION} opencv_contrib

# OpenCV build and installation
mkdir ~/tmp/opencv/build \
  && cd ~/tmp/opencv/build \
  && cmake \
    -D CMAKE_BUILD_TYPE=RELEASE \
	-D CMAKE_INSTALL_PREFIX=/usr/local \
	-D INSTALL_PYTHON_EXAMPLES=ON \
	-D INSTALL_C_EXAMPLES=OFF \
	-D OPENCV_ENABLE_NONFREE=ON \
	-D OPENCV_EXTRA_MODULES_PATH=~/tmp/opencv_contrib/modules \
	-D PYTHON_EXECUTABLE=~/dev/music-album-loader/.venv/bin/python3 \
	-D BUILD_EXAMPLES=ON .. \
  && make -j4 \
  && sudo make install \
  && sudo ldconfig
  && ln -s /usr/local/lib/python3.6/site-packages/cv2/python-3.6/cv2.cpython-36m-x86_64-linux-gnu.so ~/dev/music-album-loader/.venv/lib/python3.6/site-packages/cv2.so

# Install project dependencies
sudo apt install libzbar0
pipenv install numpy pyzbar imutils
```
