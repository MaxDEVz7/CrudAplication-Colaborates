// Função para enviar os dados para a API
document.getElementById("cadastroForm").addEventListener("submit", function(event) {
    event.preventDefault();  // Impede o envio normal do formulário

    // Coletar os dados do formulário
    const nome = document.getElementById("nome").value;
    const senha = document.getElementById("senha").value;

         // Montar o objeto de dados
         const data = {
            nome: nome,
            senha: senha
        };

    // Enviar os dados para a API usando fetch
    fetch('http://localhost:8080/colaborates', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            alert('Cadastro realizado com sucesso!');
        } else {
            alert('Erro ao cadastrar, tente novamente!');
        }
    })
    .catch(error => {
        console.error('Erro na requisição:', error);
        alert('Erro ao se conectar com a API.');
    });
});