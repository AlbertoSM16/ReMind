#!/bin/bash

NODE_DIR="$HOME/.local/nodejs"
NODE_BIN="$NODE_DIR/bin"

if command -v node &> /dev/null && [ "$(node -v | cut -d'v' -f2 | cut -d'.' -f1)" -ge 18 ]; then
  echo "Node.js $(node -v) OK"
else
  if [ -x "$NODE_BIN/node" ] && [ "$($NODE_BIN/node -v | cut -d'v' -f2 | cut -d'.' -f1)" -ge 18 ]; then
    echo "Usando Node.js $($NODE_BIN/node -v) desde $NODE_DIR"
    export PATH="$NODE_BIN:$PATH"
  else
    echo "Descargando Node.js 20 en $NODE_DIR..."
    mkdir -p "$NODE_DIR"
    wget -qO- https://nodejs.org/dist/v20.19.0/node-v20.19.0-linux-x64.tar.xz | tar -xJ -C "$NODE_DIR" --strip=1
    if [ $? -ne 0 ]; then
      echo "Error: no se pudo descargar Node.js"
      exit 1
    fi
    export PATH="$NODE_BIN:$PATH"
    echo "Node.js $($NODE_BIN/node -v) instalado"
  fi
fi

npm install && npx ng serve --host 0.0.0.0
