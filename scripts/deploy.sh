
cd ..
mvn clean package spring-boot:repackage

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/archeocat-1.0.0.jar \
    root@80.87.192.94:/SpringServer/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa root@80.87.192.94 << EOF
systemctl restart tgbot
EOF

echo 'Bye'