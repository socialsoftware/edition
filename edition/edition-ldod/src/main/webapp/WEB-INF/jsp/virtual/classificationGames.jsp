<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<div class="col-md-8 col-md-offset-2 ldod-about">

			<div class="row">
				<h3 class="text-center">
					<spring:message code="general.classificationGame" />
				</h3>
				<br />
				<p>
					<a href="../classification-game" target="_blank"><spring:message
							code="general.classificationGame.visitWebsite" /> <span
						class="glyphicon glyphicon-open"></span> </a> <br />
				</p>

				<p>
					No �mbito de uma disserta��o de mestrado desenvolvida no Instituto
					Superior T�cnico centrada em Crowdsourcing e Gamification no <em>Arquivo
						LdoD</em>, foi criado o jogo <em>LdoD Classification</em> ou Jogo de
					Classifica��o do LdoD</em>. O jogo faz uso das edi��es virtuais do
					Arquivo LdoD, sendo ent�o necess�rio estar registado para poder
					jogar e criar jogos. O objectivo � classificar fragmentos de
					edi��es virtuais atrav�s de v�rios jogos. Cada jogo corresponde a
					classificar um fragmento de uma edi��o virtual. Informa��es
					necess�rias para jogar:
				</p>
				<ul>
					<li>M�nimo de dois jogadores para um jogo ocorrer;</li>
					<li>Os jogos ocorrem a uma hora e tempo espec�fico definido na
						altura da cria��o do mesmo;</li>
					<li>A dura��o de um jogo depende do tamanho de um fragmento
						(contudo um jogo n�o demora mais do que 5 a 10
						minutos);</li>
					<li>O jogo consiste em 3 rondas, sendo que as rondas 1 e 2
						ocorrem m�ltiplas vezes (uma para cada par�grafo do fragmento):
						<ul>
							<li>Ronda 1 - Submeter: O jogador t�m no ecr� o primeiro
								par�grafo, l� e analisa o mesmo e depois submete 1 e apenas 1
								categoria que considera adequada; (avan�amos para a ronda 2 no
								fim do tempo);</li>
							<li>Ronda 2 - Votar: O tempo � agora fixo de 15 segundos, o
								utilizador t�m novamente o mesmo par�grafo que viu em 1 e v�
								tamb�m as categorias submetidas por todos os participantes e s�
								pode votar numa categoria que considera adequada (caso n�o haja
								mais par�grafos para analisar avan�amos para 3; caso contr�rio
								avan�amos para a ronda 1, agora para o par�grafo seguinte);</li>
							<li>Ronda 3 - Rever: Tempo de 30 segundos, o utilizador t�m
								no ecr� o fragmento completo e em cima as categorias mais
								votadas at� ent�o. O utilizador vota numa categoria, contudo
								pode trocar de voto enquanto o tempo estiver dispon�vel (sendo
								que trocas de voto penalizam os seus pontos) e simultaneamente
								v� os votos totais, ou seja, est� em tempo real a verificar qual
								a categoria mais votada, vendo os votos nas categorias a variar,
								consoante os participantes v�o votando. O objectivo � determinar
								a categoria mais adequada para classificar o fragmento.</li>
							<li>Acabando a ronda 3, o jogador que sugeriu a categoria
								vencedora � creditado como o autor da mesma na edi��o virtual e
								fragmento correspondente.</li>
						</ul>
					</li>
				</ul>
				<p>Passos para criar um jogo:</p>
				<ol>
					<li>Necess�rio ser gestor de uma edi��o virtual (caso n�o seja
						crie uma edi��o virtual), <strong>note que a edi��o
							virtual ter� de ter um vocabul�rio aberto </strong>
					</li>
					<li>As configura��es da edi��o virtual condicionam as
						defini��es de um jogo, ou seja: o jogo poder� ser para todos
						utilizadores registados ou apenas membros da edi��o virtual, se a
						edi��o for privada obrigatoriamente apenas membros da edi��o
						virtual podem jogar. Se a edi��o for p�blica poder� ser qualquer
						um dos casos.</li>
					<li>V� ent�o a Virtual -> Edi��es Virtuais -> Gerir (escolhe qual a edi��o que
						quer criar o jogo) -> Jogo de Classifica��o -> Criar;
					<li>Preencha os par�metros adequadamente e verifique a hora,
						fragmento escolhido e qual os jogadores que podem jogar (Membros
						da edi��o ou Qualquer utilizadores registados), clique em Criar</li>
				</ol>
				<p>
					Depois, para jogar, � necess�rio entrar <a
						href="https://ldod.uc.pt/classification-game/" target="_blank">na aplica��o jogo</a>, fazer
					login (atualmente n�o se suportam utilizadores registados atrav�s de uma rede social, 
					por exemplo, Google ou Facebook) e � hora do
					jogo, carregar para come�ar o jogo. <br /> <strong> Por
						fim, e de forma a melhorar o mesmo
						providenciando uma melhor experi�ncia, responda ao inqu�rito. O seu feedback � muito precioso para n�s!</strong> O inqu�rito
					est� dispon�vel <a
						href="https://ldod.uc.pt/classification-game/feedback" target="_blank">aqui</a>,
					ou aceda � p�gina do jogo e clique no separador Feedback.
				</p>
			</div>

		</div>
	</div>
</body>

</html>