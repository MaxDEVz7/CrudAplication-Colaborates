async function fetchData() {
    try {
        const response = await fetch('http://localhost:8080/colaborates');
        if (!response.ok) {
            throw new Error(`Erro ao acessar dados: ${response.statusText}`);
        }
        const data = await response.json();

        const tableBody = document.getElementById('data-table');

        data.forEach(item => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>${item.score}</td>
                <td>${item.chefe ? item.chefe.nome : 'Sem chefe vinculado'}</td>
            `;

            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
}

button = document.getElementById("button cadastrar");
button.addEventListener("click", function() {
    window.location.href = "http://127.0.0.1:5500/cadastrar/cadastrar.html";
});

fetchData();

