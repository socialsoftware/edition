/** @format */

export default /*html*/ `
<h4 class="text-center mb">Jogo de Classificação</h4>
<p>
    <a is="nav-to" href="${import.meta.env.VITE_NODE_HOST}/classification-game" target="_blank">
        Clique aqui para visitar o Jogo de Classificação
        <span is="ldod-span-icon" icon="arrow-up-from-bracket" fill="#0d6efd" size="16px"></span>
    </a>
</p>
<p>
    No Âmbito de uma dissertação de mestrado desenvolvida no Instituto Superior Técnico centrada em
    Crowdsourcing e Gamification no
    <em>Arquivo LdoD</em>
    , foi criado o jogo
    <em>LdoD Classification</em>
    ou Jogo de Classificação do LdoD. O jogo faz uso das edições virtuais do Arquivo LdoD, sendo
    então necessário estar registado para poder jogar e criar jogos. O objectivo é classificar
    fragmentos de edições virtuais através de vários jogos. Cada jogo corresponde a classificar um
    fragmento de uma edição virtual. Informações necessárias para jogar:
</p>
<ul>
    <li>Mí­nimo de dois jogadores para um jogo ocorrer;</li>
    <li>Os jogos ocorrem a uma hora e tempo específico definido na altura da criação do mesmo;</li>
    <li>
        A duração de um jogo depende do tamanho de um fragmento (contudo um jogo não demora mais do
        que 5 a 10 minutos);
    </li>
    <li>
        O jogo consiste em 3 rondas, sendo que as rondas 1 e 2 ocorrem múltiplas vezes (uma para
        cada parágrafo do fragmento):
        <ul>
            <li>
                Ronda 1 - Submeter: O jogador têm no ecrã o primeiro parágrafo, lê e analisa o mesmo
                e depois submete 1 e apenas 1 categoria que considera adequada; (avançamos para a
                ronda 2 no fim do tempo);
            </li>
            <li>
                Ronda 2 - Votar: O tempo é agora fixo de 15 segundos, o utilizador têm novamente o
                mesmo parágrafo que viu em 1 e vê também as categorias submetidas por todos os
                participantes e só pode votar numa categoria que considera adequada (caso não haja
                mais parágrafos para analisar avançamos para 3; caso contrário avançamos para a
                ronda 1, agora para o parágrafo seguinte);
            </li>
            <li>
                Ronda 3 - Rever: Tempo de 30 segundos, o utilizador têm no ecrã o fragmento completo
                e em cima as categorias mais votadas até então. O utilizador vota numa categoria,
                contudo pode trocar de voto enquanto o tempo estiver disponí­vel (sendo que trocas
                de voto penalizam os seus pontos) e simultaneamente vê os votos totais, ou seja,
                está em tempo real a verificar qual a categoria mais votada, vendo os votos nas
                categorias a variar, consoante os participantes vão votando. O objectivo é
                determinar a categoria mais adequada para classificar o fragmento.
            </li>
            <li>
                Acabando a ronda 3, o jogador que sugeriu a categoria vencedora é creditado como o
                autor da mesma na edição virtual e fragmento correspondente.
            </li>
        </ul>
    </li>
</ul>
<p>Passos para criar um jogo:</p>
<ol>
    <li>
        Necessário ser gestor de uma edição virtual (caso não seja crie uma edição virtual),
        <strong>note que a edição virtual terá de ter um vocabulário aberto</strong>
    </li>
    <li>
        As configurações da edição virtual condicionam as definições de um jogo, ou seja: o jogo
        poderá ser para todos utilizadores registados ou apenas membros da edição virtual, se a
        edição for privada obrigatoriamente apenas membros da edição virtual podem jogar. Se a
        edição for pública poderá ser qualquer um dos casos.
    </li>
    <li>
        Vá então a Virtual -&gt; Edições Virtuais -&gt; Gerir (escolhe qual a edição que quer criar
        o jogo) -&gt; Jogo de Classificação -&gt; Criar;
    </li>
    <li>
        Preencha os parâmetros adequadamente e verifique a hora, fragmento escolhido e qual os
        jogadores que podem jogar (Membros da edição ou Qualquer utilizadores registados), clique em
        Criar
    </li>
</ol>
<p>
    Depois, para jogar, é necessário entrar
    <a href="https://ldod.uc.pt/classification-game/" target="_blank">na aplicação jogo</a>
    , fazer login (atualmente não se suportam utilizadores registados através de uma rede social,
    por exemplo, Google ou Facebook) e à hora do jogo, carregar para começar o jogo.
    <br />
    <strong>
        Por fim, e de forma a melhorar o mesmo providenciando uma melhor experiência, responda ao
        inquérito. O seu feedback é muito precioso para nós!
    </strong>
    O inquérito está disponí­vel
    <a href="https://ldod.uc.pt/classification-game/feedback" target="_blank">aqui</a>
    , ou aceda à página do jogo e clique no separador Feedback.
</p>
`;
