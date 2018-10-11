<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="isAuthenticated"
		value="${pageContext.request.userPrincipal.authenticated}" />

	<div class="container">
		<div class="row">
			<h3 class="text-center">
				<spring:message code="general.classificationGame" />
			</h3>
			<br>
			
			<div class="row">
				<a href="../classification-game" target="_blank"><spring:message code="general.classificationGame.visitWebsite" />
					<span class="glyphicon glyphicon-open"></span>
				</a>
				<br/>
			</div>
			<br>

			<br class="row">
				No âmbito de uma dissertação de mestrado desenvolvida no Instituto Superior Técnico centrada em Crowdsourcing e Gamification no <em>Arquivo LdoD</em>, foi criado o jogo <em>LdoD Classification</em> ou Jogo de Classifcação do LdoD</em>.
				O jogo faz uso das edições virtuais do Arquivo LdoD, sendo então necessário estar registado para poder jogar e criar jogos.
				O objectivo é classificar fragmentos de edições virtuais através de vários jogos.
				Cada jogo corresponde a classificar um fragmento de uma edição virtual.
				Informações acerca de um jogo:
				<ul>
					<li> mínimo de dois jogadores para um jogo ocorrer; </li>
					<li> os jogos ocorrem a uma hora e tempo especifico definido na altura da criação do mesmo;</li>
					<li> a duração de um jogo depende um pouco do tamanho de um fragmento (contudo um jogo não deve demorar muito mais do que uns 5 minutos); </li>
					<li> o jogo consiste em 3 rondas, sendo que as rondas 1 e 2 ocorrem múltiplas vezes (uma para cada parágrafo do fragmento):
						<ul>
							<li> Ronda 1 - Submeter: O jogador têm no ecrã o primeiro parágrafo, lê e analisa o mesmo e depois submete 1 e apenas 1 categoria que considera adequada; (avançamos para a ronda 2 no fim do tempo);</li>
							<li> Ronda 2 - Votar: O tempo é agora fixo de 15 segundos, o utilizador têm novamente o mesmo parágrafo que viu em 1 e vê também as categorias submetidas por todos os participantes e só pode votar numa categoria que considera adequada (caso não haja mais parágrafos para analisar avançamos para 3; caso contrário avançamos para a ronda final - 3);</li>
							<li> Ronda 3 - Rever: Tempo de 30 segundos, o utilizador têm no ecrã o fragmento completo na parte e baixo e em cima as categorias mais votadas até então. O utilizador vota numa categoria, contudo pode trocar de voto enquanto o tempo estiver disponível (sendo que trocas de voto penalizam os seus pontos) e simultaneamente vê os votos totais, ou seja, está em tempo real a verificar qual a tag mais votada e vê tags a subir ou descer consoante os votos dos outros. O objectivo é determinar a categoria mais adequada.
							</li>
						<li> Acabando a ronda 3, o jogador que sugeriu a tag vencedora é creditado como o autor da mesma na edição virtual e fragmento correspondente.</li>
						</ul>
					</li>
				</ul>
				<br/>
				Passos para criar um jogo:
					<ol>
						<li> Necessário ser gestor de uma edição virtual (caso não seja crie uma edição virtual), <strong>note que a edição virtual terá de ter um vocabulário aberto </strong> </li>
						<li> As configurações da edição virtual condicionam as definições de um jogo, ou seja:
							o jogo poderá ser para todos utilizadores registados ou apenas membros da edição virtual, se a edição for privada obrigatoriamente apenas membros da edição virtual podem jogar.
							Se a edição for pública poderá ser qualquer dos casos.</li>
						<li> Vá então a Virtual -> Gerir (escolhe qual a edição que quer criar o jogo)  -> Jogo de Classificação -> Criar;
						<li> Preencha os parâmetros adequadamente e verifique a hora, fragmento escolhido e qual os jogadores que podem jogar (Membros da edição ou Todos os utilizadores registados), clique em Criar</li>
					</ol>
				<br/>
				Depois disto é necessário entrar <a href="https://ldod.uc.pt/classification-game/">aqui.</a>, fazer o login (apenas logins para contas de e-mail de momento) e à hora do jogo, carregar para começar o jogo.
				<br/>

				<strong> Por fim, no fim do seu jogo e de forma a melhorar o mesmo providenciando uma melhor experiência , responde ao inquérito sobre o mesmo. O seu feedback é muito precioso para nós!</strong>
				O inquérito está disponível em <a href="https://ldod.uc.pt/classification-game/feedback">aqui.</a> ou aceda à página do jogo e clique no separador Feedback.
			</div>

			<br>


		</div>
	</div>
</body>

</html>