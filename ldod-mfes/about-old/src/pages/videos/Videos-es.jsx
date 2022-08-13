import { useEffect } from 'react';
export default ({ scroll, posY }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);

  return (
    <>
      <h1 className="text-center">Vídeos</h1>
      <p>&nbsp;</p>

      <p>
        Esta p&aacute;gina contiene v&iacute;deos con testimonios de dos
        editores del
        <em>Libro del desasosiego</em> y del editor del <em>Archivo LdoD</em>.
        Teresa Sobral Cunha y Jer&oacute;nimo Pizarro fueron entrevistados en
        noviembre y diciembre de 2015, respectivamente. Manuel Portela fue
        entrevistado en agosto de 2017. Las entrevistas fueron editadas de
        acuerdo con temas espec&iacute;ficos relativos a la historia material y
        editorial del <em>Libro del desasosiego</em>, y a la naturaleza del{' '}
        <em>Archivo LdoD</em> como representaci&oacute;n y simulaci&oacute;n de
        las ediciones del Libro. En el caso de los editores del <em>Libro</em>,
        la entrevista se centr&oacute; en un conjunto de cuestiones sobre el
        primer contacto con los materiales del <em>Libro</em>, la
        concetualizaci&oacute;n de un modelo para el <em>Libro</em>, los
        criterios de selecci&oacute;n y ordenaci&oacute;n de los textos, y la
        percepci&oacute;n sobre la importancia y el significado actual del{' '}
        <em>Libro del desasosiego</em>. En el caso del editor del{' '}
        <em>Archivo LdoD</em>, las preguntas se refer&iacute;an a la naturaleza
        din&aacute;mica y simulatoria del <em>Archivo</em>, y a su potencial
        como entorno textual de aprendizaje, investigaci&oacute;n y
        creaci&oacute;n. Tambi&eacute;n hay un breve v&iacute;deo de
        presentaci&oacute;n del <em>Archivo LdoD</em>, mostrando extractos de
        los aut&oacute;grafos y de las ediciones de los textos &ldquo;Amo, pelas
        tardes demoradas de ver&atilde;o&rdquo; y &ldquo;Sinfonia de uma noite
        inquieta&rdquo;, le&iacute;dos, respectivamente, por Jer&oacute;nimo
        Pizarro y Teresa Sobral Cunha. Los temas de cada v&iacute;deo
        est&aacute;n referenciados a trav&eacute;s de intert&iacute;tulos, de
        acuerdo con la secuencia que se indica a continuaci&oacute;n. Registro y
        montaje de todos los v&iacute;deos: Tiago Cravid&atilde;o.
        Financiaci&oacute;n: Fundaci&oacute;n para la Ciencia y la
        Tecnolog&iacute;a.
      </p>
      <br />

      <h5>&Iacute;ndice de los v&iacute;deos:</h5>

      <ul>
        <li>
          <a onClick={(e) => scroll('#V1')}>Teaser_LdoD: Archivo LdoD</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V2')}>
            La realidad documental est&aacute; desorganizada
          </a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V3')}>Vicente Guedes y Bernardo Soares</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V4')}>
            Creaci&oacute;n de unidades discursivas
          </a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V5')}>
            Creaci&oacute;n de p&aacute;rrafos
          </a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V6')}>Habla humana</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V7')}>&iquest;Por qu&eacute; editar?</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V8')}>
            Organizaci&oacute;n cronol&oacute;gica
          </a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V9')}>Heter&oacute;nimos</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V10')}>Posibilidades de edici&oacute;n</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V11')}>Materialidad y cr&iacute;tica</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V12')}>Lectura espont&aacute;nea</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V13')}>Hiperconciencia</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V14')}>Archivo LdoD</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V15')}>Archivo din&aacute;mico</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V16')}>Aprender Investigar Crear</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V17')}>
            Simulador de performatividad literaria
          </a>
        </li>
      </ul>

      <br />
      <h5 id="V1">Teaser_LdoD: Archivo LdoD</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/dEnavuucyrY"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V2">La realidad documental est&aacute; desorganizada</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/wHMLD23JkIw"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V3">Vicente Guedes y Bernardo Soares</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/AfTDWoOFEMA"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V4">Creaci&oacute;n de unidades discursivas</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/EZYBzryVWYI"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V5">Creaci&oacute;n de p&aacute;rrafos</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/-LGEm7qf6qc"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V6">Habla humana</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/ObI5qTT1qhU"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V7">&iquest;Por qu&eacute; editar?</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/qdc3y_pUBII"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V8">Organizaci&oacute;n cronol&oacute;gica</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/0E8-k7ZuCd8"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V9">Heter&oacute;nimos</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/sXliW-96fLw"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V10">Posibilidades de edici&oacute;n</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/CODlWW6BqhE"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V11">Materialidad y cr&iacute;tica</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/N-6lEbfFB6E"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V12">Lectura espont&aacute;nea</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/chTLiJDAhCA"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V13">Hiperconciencia</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/oAFPcx6_rbs"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V14">Archivo LdoD</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/6MiYye4rQJk"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V15">Archivo din&aacute;mico</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/7_6pKD2ktSY"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V16">Aprender Investigar Crear</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/MxycSwfE8XY"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <h5 id="V17">Simulador de performatividad literaria</h5>
      <div className="videoWrapper">
        <iframe
          width="560"
          height="315"
          src="https://www.youtube.com/embed/OF3OD3-i1EY"
          frameBorder="0"
          allowFullScreen></iframe>
      </div>
      <br />
      <br />

      <p>[actualizaci&oacute;n 01-10-2017]</p>
    </>
  );
};
