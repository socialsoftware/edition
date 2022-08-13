import { useEffect } from 'react';

export default ({ posY, scroll }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);
  return (
    <>
      <h1 className="text-center">Codificación de Texto</h1>
      <p>&nbsp;</p>
      <h3>1. La norma XML TEI</h3>
      <p>
        Todas las transcripciones del <em>Archivo LdoD </em>están codificadas de
        acuerdo con las{' '}
        <a href="http://www.tei-c.org/Guidelines/" target="new">
          <em>TEI Guidelines</em>
        </a>{' '}
        (Text Encoding and Interchange). Estas directrices designan una norma de
        codificación de textos desarrollada desde 1988 por un consorcio de
        instituciones — el{' '}
        <a href="http://www.tei-c.org/index.xml" target="new">
          Text Encoding Initiative Consortium
        </a>
        . La norma TEI define un amplio conjunto de elementos y atributos en el
        lenguaje XML (
        <a href="https://en.wikipedia.org/wiki/XML" target="new">
          Extensible Markup Language
        </a>
        ) que permiten representar características estructurales, concetuales y
        de visualización de los textos. Teniendo en cuenta que el formato XML es
        un lenguaje de codificación de textos legible por máquinas y humanos, la
        norma TEI puede describirse como un método de análisis textual para el
        procesamiento digital. Se aprovecha de la interoperabilidad del formato
        XML para describir detalladamente hechos y eventos textuales, a través
        de una sintaxis y semántica específicas. Se trata de una convención muy
        poderosa, compuesta por módulos de codificación para todos los tipos de
        problemas de representación de estructuras y formas textuales — desde
        los inicios de la escritura hasta los documentos electrónicos actuales.
        La actual versión de las{' '}
        <em>Guidelines for Electronic Text Encoding and Interchange</em> (TEI
        P5) se publicó en noviembre de 2007 y se actualiza periódicamente. En el
        momento en que escribimos, la última actualización se realizó en julio
        de 2017 (versión 3.2.0).
      </p>
      <p>
        <strong>&nbsp;</strong>
      </p>
      <h3>
        2. XML TEI aplicado al <em>Libro del desasosiego</em>
      </h3>
      <p>
        En el caso del <em>Archivo LdoD</em>, el esquema de codificación
        desarrollado implicó la adopción del método de segmentación paralela y
        la creación de un aparato crítico que trata todas las transcripciones
        como variaciones textuales. Por lo tanto, es posible comparar
        automáticamente entre sí no sólo variaciones que ocurren en los
        testimonios, sino también variaciones que ocurren entre las
        transcripciones de un testimonio en las diferentes ediciones. Aunque los
        testimonios están marcados como tal, todas las variaciones (sean de los
        originales, sean de las ediciones) se procesan en un mismo plano. A
        nivel de la codificación textual, esta decisión expresa el principio
        teórico que subyace al<em> Archivo LdoD:</em> el de mostrar el{' '}
        <em>Libro del desasosiego</em> como proyecto autoral y como proyecto
        editorial, haciendo posible observar la transformación del archivo en
        ediciones como un proceso dinámico de generación de variaciones
        textuales.
      </p>
      <p>
        La matriz de codificación XML TEI permite comprender lo que en nuestro
        modelo de virtualización del <em>Libro del desasosiego</em> describimos
        como la dimensión genética, es decir, el proceso de inscripción autoral
        (escrita y reescrita), y aquello que designamos la dimensión social,
        esto es, el proceso histórico de reinscripción editorial y socialización
        de los textos (edición y reedición) bajo formas editoriales y
        bibliográficas específicas (Portela y Silva, 2014). Por su parte, la
        dimensión virtual (que es una designación de la socialización textual
        del <em>Libro del desasosiego</em> en el contexto específico del{' '}
        <em>Archivo LdoD</em>) consiste en la posibilidad de construir ediciones
        virtuales a través de la combinación de las unidades textuales definidas
        por las interpretaciones autorales y/o por las interpretaciones
        editoriales (heredando por lo tanto la estructura XML TEI de esas
        unidades). El dinamismo del <em>Archivo LdoD</em> depende de la
        modularización producida por su modelo de datos, pero también de la
        versatilidad en la comparación de transcripciones garantizada por su
        modelo de codificación textual.
      </p>
      <p>
        Por lo tanto, la codificación XML TEI se utiliza simultáneamente en su
        potencial representacional — es decir, en la posibilidad de una
        descripción granular de eventos específicos en la superficie de
        inscripción — y en su potencial relacional — es decir, en la posibilidad
        de lectura instantánea de micro y macro-variaciones a diferentes escalas
        textuales dentro del corpus. Es como si todas las transcripciones de
        todos los testimonios y de todas las ediciones formaran parte de un solo
        árbol, haciendo palabras, signos de puntuación, espacios en blanco,
        divisiones de párrafo y divisiones de texto comparables entre sí. La
        exhaustividad y complejidad de la codificación de las variaciones
        permite transitar recursivamente entre tres escalas: escala del
        carácter, de la señal de puntuación y del espacio en blanco como
        unidades mínimas de inscripción; escala de la palabra, de la frase, del
        párrafo y del bloque textual como unidades semánticas; y escala de la
        secuencia ordenada de textos y del libro como instanciación discursiva y
        material de una cierta idea de la obra.
      </p>
      <p>
        <strong>&nbsp;</strong>
      </p>
      <h3>3. Criterios de codificación</h3>
      <p>
        El equipo del <em>Archivo LdoD </em>definió un conjunto de criterios de
        codificación de los diversos actos de inscripción autoral y editorial.
        Estos criterios están, a veces, condicionados por la necesidad de
        simplificar el procesamiento y la visualización de las marcaciones. Esta
        sección explica las principales opciones para garantizar el equilibrio
        entre el rigor textual y semántico de la marcación, por un lado, así
        como la legibilidad de la visualización de la capa genética y de las
        comparaciones entre transcripciones, por otro. Aunque haya un componente
        mimético topográfico en las marcaciones de los testimonios, este está
        subordinado al modelo abstracto de datos cuya función principal es
        garantizar la comparabilidad de las transcripciones y ordenaciones de
        los documentos de cada edición. Nuestro objetivo principal no es imitar
        los documentos sino modelarlos para procesamiento.
      </p>
      <p>
        <strong>Codificación de espacios y de líneas de división</strong>
        <br />
        Los espacios entre párrafos, entre título y texto, y entre bloques de
        texto <em>se marcaron en sus proporciones relativas</em>, tanto en lo
        que se refiere a los testimonios, como a lo que se refiere a las
        ediciones. En lugar de una sangría de la primera línea del párrafo,
        hemos optado por un espacio de intervalo entre todos los párrafos, con
        el objetivo de mejorar la legibilidad de las transcripciones en la
        lectura en pantalla. Todos los demás espacios corresponden a espacios
        que constan de los testimonios y de las ediciones. Todas las líneas de
        división corresponden también a líneas que constan de los testimonios y
        de las ediciones. Espacios y líneas son elementos importantes en la
        articulación de las divisiones internas y externas de cada texto.
      </p>
      <p>
        <strong>Codificación de eliminados</strong>
        <br />
        La marcación de texto eliminado (&lt;del&gt;) se hizo con los valores
        “overstrike” y “overtyped.” Ambos se visualizan a través de una línea
        sobre la palabra borrada.
      </p>
      <p>
        <strong>Codificación de acrecentados</strong>
        <br />
        La marcación de los acrecentados (&lt;add&gt;) se hizo con los valores
        “below”, “above”, “inline”, “top”, “bottom” y “margin”, que definen la
        posición (&lt;place&gt;) del acrecentado relativamente al segmento de
        texto al que se aplica o al lugar en que aparece en la página. El valor
        “margin” fue generalmente reservado para texto añadido en los márgenes,
        pero<em> no para texto que continúa en los márgenes</em>, como a menudo
        sucede. En estos casos el cambio de lugar del texto en la página es
        señalado sólo por quiebra de línea o por quiebra de línea y un espacio.
      </p>
      <p>
        <strong>Codificación de sustituciones</strong>
        <br />
        La codificación de sustituciones (&lt;subst&gt;) implica la marcación de
        un elemento eliminado &lt;del&gt; y de un elemento acrecentado
        &lt;add&gt;. La visualización simple de la transcripción del testimonio
        puede a veces sugerir que un elemento es un acrecentado, ya que es
        precedido por la señal ∧, utilizado para señalar los inseridos. En estos
        casos, la ambigüedad puede ser aclarada si se seleccionan
        simultáneamente las casillas “mostrar eliminados”, “realzar
        acrecentados” y “realzar sustituciones”. En el segundo párrafo del
        testimonio{' '}
        <a
          href="https://ldod.uc.pt/fragments/fragment/Fr044/inter/Fr044_WIT_MS_Fr044a_51"
          target="new">
          BNP/E3 1-50r
        </a>
        , por ejemplo, la interfaz muestra un acrecentado (dean- teira ∧d’ella)
        cuando el testimonio sólo tiene la cancelación de la terminación plural
        (dean- teira d’ellas). En este caso, la opción de marcación implicó
        explicitar la sustitución de “d’ellas” por “d’ella” (como si hubiera, de
        hecho, un acrecentado), ya que ésta es la implicación semántica de la
        eliminación. Esta regla de marcación
        <em>
          {' '}
          se aplicó a todas las ocurrencias en las que la eliminación de una
          parte de la palabra la transforma en otra palabra, por ejemplo, un
          número plural en un número singular o un género masculino en un género
          femenino.
        </em>{' '}
        Aunque no haya un añadido, la alteración es gráfica y semánticamente
        marcada como si hubiera. La desambiguación de estas opciones de
        codificación implica señalar, al mismo tiempo, las casillas “mostrar
        eliminados”, “realzar acrecentados” y “mostrar sustituciones”, así como
        la colación con el facsímil digital. Esta opción fue tomada por motivos
        de visualización, ya que el marcado de letras aisladas resultaría en la
        presentación de un espacio antes y después de la letra, perjudicando la
        legibilidad. Las opciones “mostrar eliminados” y “realzar acrecentado”
        no se sincronizaron (lo que ayudaría a aclarar esa ambigüedad) ya que su
        aplicación independiente permite ver más claramente los distintos
        lugares de revisión.
      </p>
      <p>
        <strong>Codificación de variantes</strong>
        <br />
        La marcación de las variantes se visualiza a través de la presencia
        simultánea de las alternativas en un color diferente al del resto del
        texto. Cuando se pasa sobre cada variante, surge el valor de
        probabilidad que le está asociado, que resulta de la división de la
        unidad por el número de variantes textuales. En el primer párrafo del
        testimonio{' '}
        <a
          href="https://ldod.uc.pt/fragments/fragment/Fr044/inter/Fr044_WIT_MS_Fr044a_51"
          target="new">
          BNP/E3 1-50r
        </a>
        , por ejemplo, la interfaz muestra las variantes “rancor” y “torpor” con
        una probabilidad de ser excluidas de 0.5 cada una (“excl 0.5”). Cuando
        hay tres variantes, la distribución es generalmente una combinación de
        0.3, 0.3 y 0.4. Sin embargo, hay casos en que la necesidad de combinar
        la marcación topográfica con la marcación de variantes{' '}
        <em>
          implicó la utilización de más valores que el número de variantes
          efectivas
        </em>
        , ya que las quiebras de línea no pueden ser marcados dentro de los
        valores de probabilidad y es necesario volver a marcar la variante como
        variante después de cada quiebra de línea. En estos casos, el valor de
        la probabilidad indicado es arbitrario y debe ajustarse para reflejar el
        número de variantes y no el número de quiebras de línea, como sucede. Se
        trata de una situación que se deriva de la imposibilidad de encajar esas
        dos jerarquías de marcación.
      </p>
      <p>
        <strong>Codificación de variaciones</strong>
        <br />
        La codificación adoptada permite además observar todas las variaciones
        existentes entre las interpretaciones, o sea, permite comparar la nueva
        transcripción de los testimonios con la transcripción Prado Coelho-1982
        con la transcripción Sobral Cunha-2008 con la transcripción Zenith-2012
        con la transcripción Pizarro-2010 para cada fragmento del{' '}
        <em>Libro del desasosiego.</em> El aparato crítico (&lt;app&gt;) de
        estas variaciones fue clasificado de acuerdo con cinco tipologías
        (&lt;type&gt;): “orthographic” para identificar variaciones
        ortográficas; “punctuation” para señalar variaciones de puntuación;
        “paragraph” para marcar variaciones en la división de párrafos; “style”
        para identificar variaciones de estilo tipográfico (por ejemplo, uso de
        cursiva o de mayúsculas); y “substantive” para indicar diferencias en la
        palabra transcrita (por ejemplo, cuando los expertos optan por variantes
        divergentes o leen de forma diferente determinado paso). Las variaciones
        marcadas (que se refieren a las diferencias existentes
        <em>
          {' '}
          en el conjunto de todas las interpretaciones de un determinado
          fragmento
        </em>
        ) son visibles tanto a través del realce de color, como a través de la
        tabla de variaciones relativa a cada comparación.
      </p>
      <p>
        <strong>&nbsp;</strong>
      </p>
      <h3>4. Información adicional</h3>
      Quien esté interesado en una descripción detallada de las opciones de
      codificación puede consultar la matriz de un archivo XML comentado{' '}
      <strong>
        <a target="_blank" href="/encoding/LdoD_XML-Template_ES.xml">
          AQUÍ
        </a>
      </strong>
      . Se sugiere también la lectura del artículo siguiente: António Rito Silva
      y Manuel Portela, “
      <a href="http://jtei.revues.org/1171" target="new">
        TEI4LdoD: Textual Encoding and Social Editing in Web 2.0 Environments.
      </a>
      ” <em>Journal of the Text Encoding Initiative</em> 8 (2014-2015).<p></p>
      <p>&nbsp;</p>
      <p>[actualizado 08-09-2017]</p>
    </>
  );
};
