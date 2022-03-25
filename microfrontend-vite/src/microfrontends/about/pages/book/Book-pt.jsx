import { useEffect } from 'react';

export default ({ posY, image }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);
  return (
    <>
      <h1 className="text-center">Livro</h1>
      <p>&nbsp;</p>
      <img className="center" src={image} width="75%" />

      <p>
        Manuel Portela,{' '}
        <em>
          Literary Simulation and the Digital Humanities: Reading, Editing,
          Writing{' '}
        </em>
        . New York: Bloomsbury Academic, 2022. URL:{' '}
        <a
          href="https://www.bloomsbury.com/uk/literary-simulation-and-the-digital-humanities-9781501385407/"
          target="_new">
          https://www.bloomsbury.com/uk/literary-simulation-and-the-digital-humanities-9781501385407/
        </a>
      </p>

      <p>&nbsp;</p>
      <h3>Descrição</h3>

      <p>
        Como usar o meio digital para compreender a leitura, a edição e a
        escrita enquanto processos literários? Como conceber o meio digital de
        uma forma que vai além do códice impresso? Este livro é uma tentativa de
        responder àquelas perguntas fundamentais, articulando uma nova teoria
        literária com um ambiente digital altamente dinâmico.
      </p>

      <p>
        Usando o arquivo digital da obra-prima modernista{' '}
        <em>Livro do Desassossego</em>, do escritor português Fernando Pessoa
        (1888-1935), como estudo de caso e espaço para simulação e experiência
        prática, <em>Literary Simulation and the Digital Humanities</em>
        demonstra como os modelos computacionais da textualidade podem explorar
        plenamente as complexidades da teoria literária contemporânea. Através
        de uma combinação única de especulação teórica, análise literária e
        imaginação humana, a obra representa uma intervenção crítica
        significativa e um avanço assinalável no uso de métodos digitais para
        repensar os processos de leitura e escrita literária.
      </p>

      <p>
        A saliência dada às práticas fundacionais da leitura, edição e escrita é
        relevante para vários campos, incluindo estudos literários, edição
        académica, estudos de software e humanidades digitais.
      </p>

      <p>&nbsp;</p>
      <h3>Table of Contents</h3>
      <p>
        Incipit: Evolutionary Textual Environment
        <br />
        1. From Archive to Simulator
        <br />
        2. Reading as Simulation
        <br />
        3. Editing as Simulation
        <br />
        4. Writing as Simulation
        <br />
        5. Living on in the Web
        <br />
        Explicit: No Problem Has a Solution
        <br />
        Acknowledgments
        <br />
        References
        <br />
        Index
        <br />
      </p>

      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>[atualização 31-12-2021]</p>
    </>
  );
};
