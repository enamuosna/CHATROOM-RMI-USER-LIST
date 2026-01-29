#!/bin/bash

echo "========================================"
echo "  Serveur ChatRoom RMI"
echo "========================================"
echo ""
echo "Démarrage du serveur..."
echo ""

# Se placer dans le répertoire du script
cd "$(dirname "$0")"

# Compiler le projet si nécessaire
if [ ! -d "target/classes" ]; then
    echo "Compilation du projet..."
    mvn clean compile
    if [ $? -ne 0 ]; then
        echo "Erreur lors de la compilation!"
        exit 1
    fi
fi

echo "Lancement du serveur..."
echo ""
mvn exec:java -Dexec.mainClass="com.chat.server.ChatServer"
