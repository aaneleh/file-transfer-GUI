# file-transfer-GUI

Aplicação em java para transferência de arquivos entre dispositivos na mesma rede por sockets.  
Um dispositivo agirá como o servidor, selecionando a pasta de arquivos que quer compartilhar e então abrindo o socket para aguardar a conexão do cliente.  
O dispositivo cliente deve digitir o IP do servidor e então se conectar, uma vez para receber a lista de arquivos e então novamente para receber os arquivos selecionado utilizando as checkboxes.

- Necessário configurar a variável de ambiente PORT igual para ambos servidor e cliente.
------