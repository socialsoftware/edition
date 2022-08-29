export default () => {
  return (
    <>
      <h1 class="text-center">Codificação de Texto</h1>
      <p>&nbsp;</p>

      <h3>1. A norma XML-TEI</h3>
      <p>
        Todas as transcrições do <em>Arquivo LdoD</em> estão codificadas de
        acordo com as{' '}
        <a href="http://www.tei-c.org/Guidelines/" target="new">
          <em>TEI Guidelines</em>
        </a>{' '}
        (Text Encoding and Interchange). Estas diretrizes designam uma norma de
        codificação de textos desenvolvida desde 1988 por um consórcio de
        instituições — o{' '}
        <a href="http://www.tei-c.org/index.xml" target="new">
          Text Encoding Initiative Consortium
        </a>
        . A norma TEI define um conjunto vasto de elementos e atributos na
        linguagem XML (
        <a href="https://en.wikipedia.org/wiki/XML" target="new">
          Extensible Markup Language
        </a>
        ) que permitem representar caraterísticas estruturais, concetuais e de
        visualização dos textos. Tendo em conta que o formato XML é uma
        linguagem de codificação de textos legível por máquinas e por humanos, a
        norma TEI pode ser descrita como um método de análise textual para
        processamento digital. Tira partido da interoperabilidade do formato XML
        de modo a descrever detalhadamente factos e eventos textuais, através de
        uma sintaxe e semântica específicas. Trata-se de uma convenção muito
        poderosa, composta por módulos de codificação para todos os tipos de
        problemas de representação de estruturas e formas textuais — desde os
        primórdios da escrita até aos documentos eletrónicos atuais. A versão
        atual das{' '}
        <em>Guidelines for Electronic Text Encoding and Interchange</em> (TEI
        P5) foi publicada em novembro de 2007 e continua a ser atualizada
        periodicamente. No momento em que escrevemos, a última atualização foi
        realizada em julho de 2017 (versão 3.2.0).
      </p>
      <p>
        <strong>&nbsp;</strong>
      </p>
      <h3>
        2. XML-TEI aplicado ao <em>Livro do Desassossego</em>
      </h3>
      <p>
        No caso do <em>Arquivo LdoD</em>, o esquema de codificação desenvolvido
        implicou a adoção do método de segmentação paralela e a criação de um
        aparato crítico que trata todas as transcrições como variações textuais.
        Deste modo, é possível comparar automaticamente entre si não apenas
        variações que ocorrem nos testemunhos, mas também variações que ocorrem
        entre as transcrições de um testemunho nas diferentes edições. Embora os
        testemunhos estejam marcados enquanto tal, todas variações (sejam dos
        originais, sejam das edições) são processadas num mesmo plano. Ao nível
        da codificação textual, esta decisão exprime o princípio teórico que
        subjaz ao <em>Arquivo LdoD</em>: o de mostrar o{' '}
        <em>Livro do Desassossego</em> como projeto autoral e como projeto
        editorial, tornando possível observar a transformação do arquivo em
        edições como um processo dinâmico de geração de variações textuais.{' '}
      </p>

      <p>
        A matriz de codificação XML-TEI permite compreender aquilo que no nosso
        modelo de virtualização do <em>Livro do Desassossego</em> descrevemos
        como a dimensão genética, isto é, o processo de inscrição autoral
        (escrita e reescrita), e aquilo que designámos como a dimensão social,
        isto é, o processo histórico de reinscrição editorial e socialização dos
        textos (edição e reedição) sob formas editoriais e bibliográficas
        específicas (Portela e Silva, 2014). Por seu turno, a dimensão virtual
        (que é uma designação da socialização textual do{' '}
        <em>Livro do Desassossego</em> no contexto específico do{' '}
        <em>Arquivo LdoD</em>) consiste na possibilidade de construir edições
        virtuais através da combinação das unidades textuais definidas pelas
        interpretações autorais e/ou pelas interpretações editoriais (herdando
        portanto a estrutura XML-TEI dessas unidades). O dinamismo do{' '}
        <em>Arquivo LdoD</em> depende da modularização produzida pelo seu modelo
        de dados, mas também da versatilidade na comparação de transcrições
        garantida pelo seu modelo de codificação textual.
      </p>

      <p>
        Deste modo, a codificação XML-TEI é usada simultaneamente no seu
        potencial representacional — isto é, na possibilidade de descrição
        granular de eventos específicos na superfície de inscrição — e no seu
        potencial relacional — isto é, na possibilidade de leitura instantânea
        de micro e macro-variações a diferentes escalas textuais dentro do
        corpus. É como se todas as transcrições de todos os testemunhos e
        edições fizessem parte de uma única árvore, tornando palavras, sinais de
        pontuação, espaços, divisões de parágrafo e divisões de texto
        comparáveis entre si. A exaustividade e complexidade da codificação das
        variações permite transitar recursivamente entre três escalas: escala do
        carater, do sinal de pontuação e do espaço em branco como unidades
        mínimas de inscrição; escala da palavra, da frase, do parágrafo e do
        bloco textual como unidades semânticas; e escala da sequência ordenada
        de textos e do livro enquanto instanciação discursiva e material de uma
        certa ideia da obra.{' '}
      </p>
      <p>
        <strong>&nbsp;</strong>
      </p>
      <h3>3. Critérios de codificação</h3>
      <p>
        A equipa do <em>Arquivo LdoD</em> definiu um conjunto de critérios de
        codificação dos diversos atos de inscrição autoral e editorial. Estes
        critérios foram, por vezes, condicionados pela necessidade de
        simplificar o processamento e a visualização das marcações. Esta secção
        explica as principais opções tomadas para garantir o equilíbrio entre o
        rigor textual e semântico da marcação, por um lado, e a legibilidade da
        visualização da camada genética e das comparações entre transcrições,
        por outro. Ainda que haja uma componente mimética topográfica nas
        marcações dos testemunhos, ela está subordinada ao modelo abstrato de
        dados cuja função principal é garantir a comparabilidade das
        transcrições e ordenações dos documentos em cada edição. O objetivo
        principal não é imitar os documentos mas modelá-los para efeitos de
        processamento.
      </p>

      <p>
        <strong>Codificação de espaços e de traços de divisão</strong>
        <br />
        Os espaços entre parágrafos, entre título e texto, e entre blocos de
        texto <em>foram marcados nas suas proporções relativas</em>, quer no que
        diz respeito aos testemunhos, quer no que diz respeito às edições. Em
        vez de uma indentação da primeira linha do parágrafo, optámos por um
        espaço de intervalo entre todos os parágrafos, com o objetivo de
        melhorar a legibilidade das transcrições na leitura em ecrã. Todos os
        outros espaços correspondem a espaços que constam dos testemunhos e das
        edições. Todos os traços de divisão correspondem também a traços que
        constam dos testemunhos e das edições. Espaços e traços são elementos
        importantes na articulação das divisões internas e externas de cada
        texto.
      </p>

      <p>
        <strong>Codificação de apagados</strong>
        <br />A marcação de texto cancelado (&lt;del&gt;) foi feita com os
        valores “overstrike” e “overtyped”. Ambos são visualizados através de um
        traço sobre a palavra apagada.
      </p>

      <p>
        <strong>Codificação de inseridos</strong>
        <br />A marcação dos inseridos (&lt;add&gt;) foi feita com os valores
        “below”, “above”, “inline”, “top”, “bottom” e “margin”, que definem a
        posição (&lt;place&gt;) do acrescento relativamente ao segmento de texto
        a que se aplica ou ao lugar em que surge na página. O valor “margin” foi
        geralmente reservado para texto acrescentado nas margens, mas{' '}
        <em>não para texto que continua nas margens</em>, como frequentemente
        acontece. Nestes casos a mudança de lugar do texto na página é
        assinalada apenas por quebra de linha ou por quebra de linha e um
        espaço.
      </p>

      <p>
        <strong>Codificação de substituições</strong>
        <br />A codificação de substituições (&lt;subst&gt;) implica a marcação
        de um elemento apagado (&lt;del&gt;) e de um elemento acrescentado
        (&lt;add&gt;). A visualização simples da transcrição do testemunho pode,
        por vezes, sugerir que dado elemento é um acrescento, uma vez que é
        antecedido pelo sinal ∧, usado para assinalar os inseridos. Nesses
        casos, a ambiguidade pode ser clarificada se se selecionarem, em
        simultâneo, as caixas “mostrar apagados”, “realçar inseridos” e “realçar
        substituições”. No segundo parágrafo do testemunho{' '}
        <a
          href="https://ldod.uc.pt/fragments/fragment/Fr044/inter/Fr044_WIT_MS_Fr044a_51"
          target="new">
          BNP/E3 1-50r
        </a>
        , por exemplo, a interface mostra um acrescento (dean- teira ∧d'ella)
        quando o testemunho tem apenas o cancelamento da terminação plural
        (dean- teira d’ellas). Neste caso, a opção de marcação implicou
        explicitar a substituição de “d’ellas” por “d’ella” (como se houvesse,
        de facto, um acrescento), uma vez que é essa a implicação semântica do
        cancelamento. Esta regra de marcação{' '}
        <em>
          foi aplicada a todas as ocorrências em que o cancelamento de uma parte
          da palavra a transforma noutra palavra — por exemplo, um número plural
          num número singular ou um género masculino num género feminino
        </em>
        . Embora não haja um acrescento, a alteração está gráfica e
        semanticamente marcada como se houvesse. A desambiguação destas opções
        de codificação implica assinalar, em simultâneo, as caixas “mostrar
        apagados”, “realçar inseridos” e “mostrar substituições”, assim como a
        colação com o fac-símile digital. Esta opção foi tomada por motivos de
        visualização, uma vez que a marcação de letras isoladas resultaria na
        apresentação de um espaço antes e depois da letra, prejudicando a
        legibilidade. As opções “mostrar apagados” e “realçar inseridos” não
        foram sincronizadas (o que ajudaria a esclarecer aquela ambiguidade)
        pois a sua aplicação separada permite observar mais claramente os vários
        lugares de revisão.
      </p>

      <p>
        <strong>Codificação de variantes</strong>
        <br />A marcação das variantes é visualizada através da presença
        simultânea das alternativas numa cor diferente da do resto do texto.
        Além disso, quando se passa sobre cada variante, surge o valor de
        probabilidade que lhe está associado, que resulta da divisão da unidade
        pelo número de variantes textuais. No primeiro parágrafo do testemunho{' '}
        <a
          href="https://ldod.uc.pt/fragments/fragment/Fr044/inter/Fr044_WIT_MS_Fr044a_51"
          target="new">
          BNP/E3 1-50r
        </a>
        , por exemplo, a interface mostra as variantes “rancor” e “torpor” com
        uma probabilidade de serem excluídas de 0.5 cada uma (“excl 0.5”).
        Quando há três variantes, a distribuição é geralmente uma combinação de
        0.3, 0.3 e 0.4. No entanto, há casos em que a necessidade de combinar a
        marcação topográfica com a marcação de variantes{' '}
        <em>
          implicou a utilização de mais valores do que o número de variantes
          efetivas
        </em>
        , uma vez que as quebras de linha não podem ser marcadas dentro dos
        valores de probabilidade e é necessário voltar a marcar a variante como
        variante depois de cada quebra de linha. Nestes casos, o valor da
        probabilidade indicado é arbitrário e deve ser ajustado de modo a
        refletir o número de variantes e não o número de quebras de linha, como
        acontece. Trata-se de uma situação que decorre da impossibilidade de
        encaixar aquelas duas hierarquias de marcação.
      </p>

      <p>
        <strong>Codificação de variações</strong>
        <br />A codificação adotada permite ainda observar todas as variações
        existentes entre as interpretações, ou seja, permite comparar a nova
        transcrição dos testemunhos com a transcrição Prado Coelho-1982 com a
        transcrição Sobral Cunha-2008 com a transcrição Zenith-2012 com a
        transcrição Pizarro-2010 para cada fragmento do{' '}
        <em>Livro do Desassossego</em>. O aparato crítico (&lt;app&gt;) destas
        variações foi classificado de acordo com cinco tipologias
        (&lt;type&gt;): “orthographic” para identificar variações ortográficas;
        “punctuation” para assinalar variações de pontuação; “paragraph” para
        marcar variações na divisão de parágrafos; “style” para identificar
        variações de estilo tipográfico (por exemplo, uso de itálicos ou de
        maiúsculas); e “substantive” para indicar diferenças na palavra
        transcrita (por exemplo, quando os peritos optam por variantes
        divergentes ou leem de forma diferente determinado passo). As variações
        marcadas (que dizem respeito às diferenças existentes{' '}
        <em>no conjunto de todas as interpretações de um dado fragmento</em>)
        são visualizáveis quer através do realce de cor, quer através da tabela
        de variações relativa a cada comparação.
      </p>
      <p>
        <strong>&nbsp;</strong>
      </p>
      <h3>4. Informação adicional</h3>
      <p>
        Quem estiver interessado numa descrição detalhada das opções de
        codificação pode consultar a matriz de um ficheiro XML comentado{' '}
        <strong>
          <a target="_blank" href="/encoding/LdoD_XML-Template_PT.xml">
            AQUI
          </a>
        </strong>
        . Sugere-se ainda a leitura do artigo seguinte: António Rito Silva e
        Manuel Portela, “
        <a href="http://jtei.revues.org/1171" target="new">
          TEI4LdoD: Textual Encoding and Social Editing in Web 2.0 Environments
        </a>
        .” <em>Journal of the Text Encoding Initiative</em> 8 (2014-2015).
      </p>
      <p>
        <strong>&nbsp;</strong>
      </p>
      <p>[atualizado 08-09-2017]</p>
    </>
  );
};
