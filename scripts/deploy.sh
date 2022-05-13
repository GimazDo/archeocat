
cd ..

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/archeocat-1.0.0.jar \
    root@213.226.124.244:/archeocat/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa root@213.226.124.244 << EOF
systemctl restart tgbot
EOF

echo 'Bye'
read var1