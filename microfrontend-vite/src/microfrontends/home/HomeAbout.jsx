import { useStore } from '../../store';

export default () => {
  const { language } = useStore();
  return (
    <div className="about font-monospace">
      {language === 'pt' && (
        <p>
          O Arquivo LdoD é um arquivo digital colaborativo do Livro do
          Desassossego de Fernando Pessoa. Contém imagens dos documentos
          autógrafos, novas transcriçõesdesses documentos e ainda transcrições
          de quatro edições da obra. Além da leitura e comparação das
          transcrições, o Arquivo LdoD permite que os utilizadores colaborem na
          criação de edições virtuais do Livro do Desassossego.
        </p>
      )}
      {language === 'en' && (
        <p>
          The LdoD Archive is a collaborative digital archive of the Book of
          Disquiet by Fernando Pessoa. It contains images of the autograph
          documents, new transcriptions of those documents and also
          transcriptions of four editions of the work. In addition to reading
          and comparing transcriptions, the LdoD Archive enables users to
          collaborate in creating virtual editions of the Book of Disquiet.
        </p>
      )}
      {language === 'es' && (
        <p>
          El Archivo LdoD es un archivo digital colaborativo del Libro del
          desasosiego de Fernando Pessoa. Contiene imágenes de los documentos
          originales, nuevas transcripciones de estos documentos y
          transcripciones de cuatro ediciones de la obra. Además de la lectura y
          la comparación de las transcripciones, el Archivo LdoD permite a los
          usuarios colaborar en la creación de ediciones virtuales del Libro del
          desasosiego.
        </p>
      )}
    </div>
  );
};
