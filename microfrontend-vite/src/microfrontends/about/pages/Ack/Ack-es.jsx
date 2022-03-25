import { useEffect } from 'react';

export default ({ posY }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);
  return (
    <>
      <h1 className="text-center">Agradecimientos</h1>
      <p>&nbsp;</p>
      <h3>Patrocinadores</h3>
      <p>
        El <em>Archivo LdoD</em> es un archivo digital colaborativo patrocinado
        por la Fundación para la Ciencia y la Tecnología (FCT), y apoyado por el
        Centro de Literatura Portuguesa de la Universidad de Coimbra (CLP), el{' '}
        <em>
          Instituto de Engenharia de Sistemas e Computadores, Investigação e
          Desenvolvimento em Lisboa
        </em>
        (INESC-ID), la Biblioteca Nacional de Portugal (BNP) y el Programa de
        doctorado en Materialidades de la Literatura (MATLIT).
      </p>
      <p>
        El <em>Archivo LdoD</em> ha sido desarrollado en el ámbito del proyecto
        de investigación “Ningún problema tiene solución: Un archivo digital del{' '}
        <em>Libro del desasosiego</em>”, coordinado por Manuel Portela
        (PTDC/CLE-LLI/118713/2010). Proyecto financiado por la Fundación para la
        Ciencia y la Tecnología (FCT) y cofinanciado por el Fondo Europeo de
        Desarrollo Regional (FEDER), a través de la Prioridad I del Programa
        Operativo de los Factores de Competitividad (POFC) del QREN—Unión
        Europea (COMPETE: FCOMP-01-0124-FEDER-019715). Financiado también por
        Fondos Nacionales a través de la Fundación para la Ciencia y la
        Tecnología en el ámbito de los proyectos de “Financiación
        Plurianual—Unidad 759”: “PEst-OE/ELT/UI0759/2013” y
        “PEst-OE/ELT/UI0759/2014”.
      </p>
      <p>&nbsp;</p>
      <h3>Colaboración</h3>A lo largo de su período de desarrollo, el{' '}
      <em>Archivo LdoD</em> se benefició de la colaboración de diversos
      individuos e instituciones. Los agradecimientos son debidos, en primer
      lugar, a los editores Jerónimo Pizarro, Richard Zenith y Teresa Sobral
      Cunha, por la consultoría como especialistas en los problemas editoriales
      del <em>Libro del Desasosiego</em>. Nuestro equipo de consultores de
      proyecto incluyó tres grandes especialistas en humanidades digitales cuya
      visión crítica contribuyó al avance de nuestras ideas: Johanna Drucker,
      Matthew G. Kirschenbaum y Susan Schreibman han compartido generosamente
      sus conocimientos y fueron una fuente de inspiración e incentivo. También
      agradecemos la contribución de Timothy Thompson a la definición inicial
      del esquema de codificación TEI. El <em>Archivo LdoD</em> agradece la
      colaboración del Archivo de Cultura Portuguesa Contemporánea (Biblioteca
      Nacional de Portugal), de la Biblioteca Pública y Archivo Regional de
      Ponta Delgada (Dirección Regional de Cultura del Gobierno de las Azores) y
      del Servicio de Gestión de Sistemas e Infraestructuras de Información y
      Comunicación (SGSIIC) de la Universidad de Coimbra. Agradece además a
      todos los usuarios beta que, en septiembre de 2014 y en mayo de 2017,
      participaron en las pruebas de las funcionalidades y del diseño web del{' '}
      <em>Archivo LdoD</em>.<p></p>
      <p>
        Estos agradecimientos se extienden a los organizadores y participantes
        de seminarios y encuentros científicos nacionales e internacionales, en
        los que se presentaron los diversos componentes concetuales y técnicos
        del <em>Archivo LdoD</em>, en particular en universidades y centros de
        investigación en Portugal, Australia, Chipre, Italia, Francia, España,
        Colombia, Estados Unidos, Suecia y Grecia. La interacción con diferentes
        comunidades de investigadores (humanidades digitales, ciencias de la
        computación, ciencias de la información, codificación textual, crítica
        textual, estudios pessoanos, literatura electrónica) fue esencial para
        el resultado alcanzado. Destacamos las siguientes presentaciones:
      </p>
      <ul>
        <h4>2012</h4>
        <ul>
          <li>
            Universidad de Coimbra, Centro de Literatura Portuguesa (CLP):
            Coloquio “Estranhar Pessoa com as Materialidades da Literatura” (25
            de mayo de 2012; coords. Manuel Portela y Osvaldo Manuel Silvestre)
          </li>
        </ul>

        <h4>2013</h4>
        <ul>
          <li>
            Universidad Nova de Lisboa, Facultad de Ciencias Sociales y Humanas
            (FCSH): VI Seminário “
            <a
              href="http://elab.fcsh.unl.pt/actividades/estranhar-pessoa-vi-seminario"
              target="new">
              Assuntos Materiais
            </a>
            ”, organizado por el Proyecto “Estranhar Pessoa” (7 de febrero de
            2013; coords. António M. Feijó y Abel Barros Baptista)
          </li>
          <li>
            Universidad de Western Sydney, Facultad de Humanidades y Artes de la
            Comunicación: Simposio “Surface Tensions: Literature in the
            Database”, organizado por el Proyecto “
            <a
              href="https://www.westernsydney.edu.au/writing_and_society/research/past_research_projects/creative_nation_writers_and_writing_in_the_new_media_culture"
              target="new">
              Creative Nation: Writers and Writing in the New Media Culture
            </a>
            ” (10 de junio de 2013; coords. Anna Gibbs y Maria Angel){' '}
          </li>
          <li>
            Universidad de Chipre: “
            <a href="https://ecscw2013.cs.ucy.ac.cy/index.php" target="new">
              ECSCW 2013: European Conference on Computer-Supported Cooperative
              Work
            </a>
            ” (21-25 de septiembre de 2013; coord. George Angelos Papadopoulos)
          </li>
          <li>
            Universidad de Roma La Sapienza: “
            <a href="http://digilab2.let.uniroma1.it/teiconf2013/" target="new">
              The Linked TEI: Text Encoding in the Web
            </a>
            ”, organizado por DIGILAB, Università La Sapienza, y Text Encoding
            Initiative Consortium (2-5 de octubre de 2013; coords. Fabio Ciotti
            y Arianna Ciula)
          </li>
          <li>
            Instituto Universitario da Maia, Centro de Investigación en
            Tecnologías y Estudios Intermedia (CITEI): Jornada “Desafios e
            Oportunidades da Edição Digital” (7 de noviembre de 2013; coords.
            Célia Vieira y Isabel Rio Novo)
          </li>
          <li>
            École Normale Supérieure, Paris, Institut des textes et manuscrits
            modernes (ITEM): “
            <a
              href="https://textualscholarship.files.wordpress.com/2015/04/programme-ests-paris-conference-2013.pdf"
              target="new">
              Variance in Textual Scholarship and Genetic Criticism/ La variance
              en philologie et dans la critique génétique
            </a>{' '}
            ”, 10th Conference of the European Society for Textual Scholarship
            (22-24 de noviembre de 2013; coords. Dirk Van Hulle y Pierre Marc de
            Biasi)
          </li>
        </ul>

        <h4>2014</h4>
        <ul>
          <li>
            Universidad de Coimbra, Biblioteca Geral: Congreso internacional “
            <a
              href="http://www.uc.pt/bguc/500anos/Congresso_internacional"
              target="new">
              A Biblioteca da Universidade: Permanências e Metamorfoses
            </a>
            ” (16-18 de enero de 2014; coord. J. A. Cardoso Bernardes){' '}
          </li>
          <li>
            Fundación Calouste Gulbenkian: Coloquio internacional “
            <a href="http://estranharpessoa.com/programa" target="new">
              O Dia Triunfal de Fernando Pessoa
            </a>
            ”, organizado por el Proyecto Estranhar Pessoa, Laboratorio de
            Estudios Literarios Avanzados (ELAB), Red de Filosofía y Literatura,
            Instituto de Filosofía del Lenguaje (IFL) y Programa en Teoría de la
            Literatura (6-8 de março de 2014; coords. António M. Feijó y Abel
            Barros Baptista)
          </li>
          <li>
            Universidad de Los Andes, Bogotá: Coloquio internacional “
            <a
              href="https://ilusionymaterialidad.wordpress.com/programa-2/"
              target="new">
              Ilusión y materialidad de los archivos literarios
            </a>
            ”, organizado por la Universidad de los Andes, Instituto Caro y
            Cuervo y Biblioteca Luis Ángel Arango (6-8 de mayo de 2014; coord.
            Jerónimo Pizarro)
          </li>
          <li>
            Universidad de Salamanca: Seminario Permanente “Arcádia Babélica”,
            organizado por el Departamento de Filologia de la Universidad de
            Salamanca (20 de junio de 2014; coord. Pedro Serra)
          </li>
          <li>
            Northwestern University, Evanston, Illinois: Coloquio internacional
            “
            <a href="http://tei.northwestern.edu/" target="new">
              Decoding the Encoded
            </a>
            ”, organizado por el Text Encoding Initiative Consortium (22-24 de
            octubre de 2014; coord. Martin Mueller)
          </li>
        </ul>

        <h4>2015</h4>
        <ul>
          <li>
            University of Grenoble: Symposium “
            <a
              href="http://www.nedimah.eu/reports/toward-new-social-contract-between-publishers-and-editors"
              target="new">
              Toward a New Social Contract between Publishers and Editors
            </a>
            ,” organizado por la Network for Digital Methods in the Arts and
            Humanities—NeDiMAH (European Science Foundation), Maison des
            Sciences de l’Homme and Université de Grenoble (26 enero de 2015;
            coords. Matthew Driscol y Elena Pierazzo)
          </li>
          <li>
            Universidad de Roma La Sapienza: Simposio “
            <a
              href="http://www.disp.let.uniroma1.it/archivionotizie/ecd/dce-edizioni-confronto/comparing-editions"
              target="new">
              Edizioni Critiche Digitali: Edizioni a Confronto / Digital
              Critical Editions: Comparing Editions
            </a>
            ”, organizado por el Dipartimento di Studi Greco-Latini, Italiani,
            Scenico-Musicali, Universitá la Sapienza, Roma (27 marzo de 2105;
            coords. Paola Italia y Claudia Bonsi)
          </li>
          <li>
            Universidad de Georgia, Athens, GA, Wilson Center for Humanities and
            Arts: Simposio “
            <a
              href="https://willson.uga.edu/event/textual-machines-a-spring-symposium-exhibit/"
              target="new">
              Textual Machines
            </a>
            ” (17-18 de abril, 2015; coord. Jonathan Baillehache)
          </li>
          <li>
            Universidad de Coimbra, Centro de Literatura Portuguesa (CLP):
            Coloquio internacional “
            <a href="https://eld2015.wordpress.com/programme/" target="new">
              Digital Literary Studies | Estudos Literários Digitais
            </a>
            ” (14-15 de mayo de 2015; coord. Manuel Portela)
          </li>
          <li>
            Universidad de Lisboa, Instituto de Ciências Sociais (ICS): Coloquio
            “Cultura e Digital em Portugal em 2015” (17 de junio de 2015;
            coords. José Luís Garcia, João Teixeira Lopes y Teresa Duarte
            Martinho)
          </li>
          <li>
            Universidad de Gotemburgo, Centro de Humanidades Digitales:{' '}
            <a
              href="http://cdh.hum.gu.se/Aktuellt/e/?eventId=2355210788"
              target="new">
              Seminario
            </a>{' '}
            (24 de septiembre de 2015; coord. Jenny Bergenmar)
          </li>
          <li>
            Universidad Nova de Lisboa, Facultad de Ciencias Sociales y Humanas
            (FCSH): Congreso internacional “
            <a
              href="https://congressohdpt.wordpress.com/programa/"
              target="new">
              Humanidades Digitais em Portugal: Construir Pontes e Quebrar
              Barreiras na Era Digital
            </a>
            ” (8-9 de octubre de 2015; coord. Daniel Alves)
          </li>
          <li>
            Universidad de Coimbra, Centro de Historia de la Sociedad y la
            Cultura (CHSC): Coloquio internacional “
            <a
              href="http://ahlist.org/conferences/2015-ahlist-coimbra/program/"
              target="new">
              Consilience and Inclusion: Scientific and Cultural Encounters
            </a>
            <a
              href="http://ahlist.org/conferences/2015-ahlist-coimbra/"
              target="new"></a>
            ”, organizado por la Asociación de Historia, Literatura, Ciencia, y
            Tecnología (19-21 de noviembre de 2015; coord. Yonsoo Kim)
          </li>
          <li>
            Universidad de Coimbra, Facultad de Economia: Coloquio “
            <a
              href="https://www.uc.pt/feuc/noticias/2015/novembro15/20151123"
              target="new">
              On/Off: Navegando pelas Culturas Digitais, Tecnologia e
              Conhecimento
            </a>
            ”, organizado por la Sección "Arte, Cultura y Comunicación" de la
            Asociación Portuguesa de Sociología (26 de noviembre de 2015;
            coords. Claudino Ferreira y Paula Abreu)
          </li>
        </ul>
        <h4>2016</h4>
        <ul>
          <li>
            Universidad Aristóteles de Salónica, Departamento de Estudios
            Americanos: Seminario (28 de enero de 2016; coord. Tatiani
            Raptzikou){' '}
          </li>
          <li>
            Universidad de Maryland, College Park, Maryland Institute for
            Technology in the Humanities (MITH): Seminario de investigación (29
            de marzo de 2016; coord. Neil Fraistat)
          </li>
          <li>
            Universidad de Maryland, College Park, English Department, Center
            for Comparative and Literary Studies (CCLS):{' '}
            <a href="http://www.english.umd.edu/events/23271" target="new">
              Conferencia de investigación
            </a>{' '}
            (14 de abril de 2016; coord. Orrin Wang)
          </li>
          <li>
            Rochester Institute of Technology, Rochester, NY, Digital Humanities
            and Social Sciences Program and School of Media Sciences:
            Conferencia (19 de abril de 2016; coord. Sandy Baldwin)
          </li>
          <li>
            Universidad de Pisa, Informatica Umanistica:{' '}
            <a
              href="http://www.labcd.unipi.it/seminari/silvestre-a-digital-archive-of-fernando-pessoa/"
              target="new">
              Seminario di Cultura Digitale
            </a>{' '}
            (7 de diciembre de 2016; coord. Enrica Salvatori)
          </li>
        </ul>
        <h4>2017</h4>
        <ul>
          <li>
            Universidad de Lisboa, Facultad de Letras:{' '}
            <a
              href="http://www.letras.ulisboa.pt/pt/agenda/conferencia-reimaginar-a-edicao-digital-no-arquivo-livro-do-desassossego"
              target="new">
              Conferencia del Programa em Crítica Textual
            </a>{' '}
            (24 de enero de 2017; coords. Esperança Cardeira, Cristina Sobral y
            João Dionísio)
          </li>
          <li>
            Universidad de la California, Los Angeles, Graduate School of
            Education and Information Studies:{' '}
            <a
              href="https://is.gseis.ucla.edu/research/colloquium/"
              target="new">
              Colloquium: Breslauer lecture series
            </a>{' '}
            (2 de febrero de 2017; coord. Johanna Drucker)
          </li>
          <li>
            Fundación Calouste Gulbenkian:{' '}
            <a
              href="http://casafernandopessoa.cm-lisboa.pt/fileadmin/CASA_FERNANDO_PESSOA/AF_CFP_Congresso_Internacional_FP_2017_Programa_Digital_V3.pdf"
              target="new">
              IV Congreso Internacional Fernando Pessoa
            </a>
            , organizado por la Casa Fernando Pessoa (9-11 de febrero de 2017;
            coord. Clara Riso)
          </li>
          <li>
            Universidad Fernando Pessoa, Porto: Coloquio internacional “
            <a
              href="https://conference.eliterature.org/2017/conference"
              target="new">
              ELO 2017: Affiliations, Communities, Translations
            </a>
            ” (18-22 de julio de 2017; coords. Rui Torres y Sandy Baldwin)
          </li>
          <li>
            Universidad Complutense de Madrid, Facultad de Educación: Simposio
            internacional de Humanidades Digitales del Sur “
            <a href="https://www.ucm.es/leethi/esc-programa" target="new">
              Escritura Creativa Digital y Colecciones Digitales
            </a>
            ” organizado por el grupo de investigación LEETHI-Literaturas
            Españolas y Europeas del Texto al Hipermedia (13-15 de septiembre de
            2017; coord. Amelia Sanz)
          </li>
        </ul>
      </ul>
      <p>&nbsp;</p>
      <h3>Herramientas Usadas</h3>
      <ul>
        <li>
          <a href="http://annotatorjs.org/">Annotator</a>
        </li>
        <li>
          <a href="http://getbootstrap.com/">Bootstrap</a>
        </li>
        <li>
          <a href="https://github.com/wenzhixin/bootstrap-table">
            Bootstrap Table
          </a>
        </li>
        <li>
          <a href="https://eclipse.org/">Eclipse</a>
        </li>
        <li>
          <a href="https://fenix-framework.github.io/">Fénix Framework</a>
        </li>
        <li>
          <a href="https://github.com/">GitHub</a>
        </li>
        <li>
          <a href="https://www.java.com/">Java</a>
        </li>
        <li>
          <a href="http://www.oracle.com/technetwork/java/javaee/jsp/index.html">
            JavaServer Pages
          </a>
        </li>
        <li>
          <a href="http://jblas.org/">jblas</a>
        </li>
        <li>
          <a href="http://jmeter.apache.org/">JMeter</a>
        </li>
        <li>
          <a href="https://jquery.com/">jQuery</a>
        </li>
        <li>
          <a href="http://junit.org/">JUnit</a>
        </li>
        <li>
          <a href="https://lucene.apache.org/">Lucene</a>
        </li>
        <li>
          <a href="http://mallet.cs.umass.edu/">Mallet</a>
        </li>
        <li>
          <a href="https://maven.apache.org/">Maven</a>
        </li>
        <li>
          <a href="https://www.mysql.com/">MySQL</a>
        </li>
        <li>
          <a href="http://openseadragon.github.io">OpenSeadragon</a>
        </li>
        <li>
          <a href="https://www.oxygenxml.com/">Oxygen</a>
        </li>
        <li>
          <a href="http://github.com/mleibman/slickgrid">SlickGrid</a>
        </li>
        <li>
          <a href="https://projects.spring.io/spring-boot/">Spring Boot</a>
        </li>
        <li>
          <a href="http://projects.spring.io/spring-social/">Spring Social</a>
        </li>
        <li>
          <a href="https://tomcat.apache.org/">Tomcat</a>
        </li>
      </ul>
      <p>&nbsp;</p>
      <p>[actualización 18-08-2017]</p>
      <p>&nbsp;</p>
    </>
  );
};
