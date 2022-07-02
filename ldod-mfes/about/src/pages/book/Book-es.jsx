import { useEffect } from 'react';

export default ({ posY, image }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);
  return (
    <>
      <h1 className="text-center">Libro</h1>
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
      <h3>Descripción</h3>

      <p>
        ¿Cómo podemos utilizar los medios digitales para entender la lectura, la
        edición y la escritura como procesos literarios? ¿Cómo podemos diseñar
        el medio digital de una manera que va más allá del códice impreso? Este
        libro es un intento de responder a estas preguntas fundamentales
        articulando una nueva teoría literaria con un entorno digital altamente
        dinámico.
      </p>

      <p>
        Utilizando el archivo digital de la obra maestra modernista{' '}
        <em>Libro del desasosiego</em>, del escritor portugués Fernando Pessoa
        (1888-1935), como caso de estudio y lugar para simulación y experimento
        práctico, <em>Literary Simulation and the Digital Humanities</em>{' '}
        demuestra cómo los modelos computacionales de la textualidad pueden
        asimilar plenamente las complejidades de la teoría literaria
        contemporánea. A través de una una combinación única de especulación
        teórica, análisis literario e imaginación humana, la obra representa una
        importante intervención crítica y un avance clave en el uso de métodos
        digitales para repensar los procesos de lectura y escritura literaria.
      </p>

      <p>
        El énfasis dado a las prácticas fundacionales de lectura, edición y
        escritura es relevante para varios campos, incluyendo estudios
        literarios, edición académica, estudios de software y humanidades
        digitales.
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
      <p>[actualización 31-12-2021]</p>
    </>
  );
};
