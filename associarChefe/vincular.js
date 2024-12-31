document.addEventListener('DOMContentLoaded', function() {
    const subordinadoSelect = document.getElementById('subordinado');
    const chefeSelect = document.getElementById('chefe');

    // Função para preencher as caixas de seleção com os dados da API
    function preencherSelects(data) {
        data.forEach(item => {
            const optionSubordinado = document.createElement('option');
            optionSubordinado.value = item.id;
            optionSubordinado.textContent = item.nome;
            subordinadoSelect.appendChild(optionSubordinado);

            const optionChefe = document.createElement('option');
            optionChefe.value = item.id;
            optionChefe.textContent = item.nome;
            chefeSelect.appendChild(optionChefe);
        });
    }

    // Buscar dados da API e preencher as caixas de seleção
    fetch('http://localhost:8080/colaborates')
        .then(response => response.json())
        .then(data => preencherSelects(data))
        .catch(error => console.error('Erro ao buscar dados:', error));

    // Função para enviar o vínculo para a API
    document.getElementById('vincularForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const idSubordinado = subordinadoSelect.value;
        const idChefe = chefeSelect.value;

        const data = {
            idChefe: idChefe,
            idSubordinado: idSubordinado
        };

        fetch('http://localhost:8080/colaborates/Associar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                alert('Vínculo realizado com sucesso!');
            } else {
                alert('Erro ao realizar vínculo, tente novamente!');
            }
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
            alert('Erro ao se conectar com a API.');
        });
        
    });
});

