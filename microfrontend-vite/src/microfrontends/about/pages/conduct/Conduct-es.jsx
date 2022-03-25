import { useEffect } from 'react';

export default ({ posY }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);
  return (
    <div>
      <p>
        Las funcionalidades de edici&oacute;n virtual y de escritura virtual son
        parte integrante de los niveles din&aacute;micos del ambiente textual
        colaborativo del <em>Archivo LdoD</em>. Se destinan a ser utilizadas en
        pr&aacute;cticas de aprendizaje, investigaci&oacute;n y creaci&oacute;n.
        Los usuarios registrados pueden crear, anotar y publicar libremente sus
        propias ediciones virtuales. Una vez que la funcionalidad de escritura
        est&eacute; disponible, tambi&eacute;n son libres de escribir y publicar
        variaciones basadas en referencias textuales particulares del{' '}
        <em>Libro del desasosiego</em>. Todos los usuarios registrados del{' '}
        <em>Archivo LdoD </em> deben respetar las reglas de conducta
        establecidas en este documento.
      </p>
      <strong>&nbsp;</strong>
      <p>
        Al registrarse como usuario del <em>Archivo LdoD</em>, usted est&aacute;
        de acuerdo en no usar la plataforma para:
      </p>
      <ol>
        <li>
          publicar cualquier contenido que sea ilegal, doloso, ofensivo,
          difamatorio, obsceno, invasivo de la privacidad de otro, de
          incitaci&oacute;n al odio, o cuyo contenido no respete los derechos
          sexuales y de g&eacute;nero, as&iacute; como los derechos
          &eacute;tnicos y raciales.
        </li>
        <li>
          publicar cualquier contenido que no tenga derecho a poner a
          disposici&oacute;n bajo cualquier ley o bajo relaciones contractuales
          espec&iacute;ficas.
        </li>
        <li>
          publicar cualquier contenido que infrinja cualquier patente, marca
          comercial, derechos de autor u otros derechos de propiedad de
          cualquier parte.
        </li>
        <li>
          publicar cualquier publicidad, material promocional,
          &ldquo;spam&rdquo;, o cualquier otra forma de solicitud.
        </li>
        <li>
          publicar cualquier material que contenga virus o cualquier otro
          c&oacute;digo de computadora, archivos o programas diseñados para
          interrumpir, destruir o limitar la funcionalidad de cualquier software
          o hardware o equipos de telecomunicaciones.
        </li>
        <li>
          interferir con o interrumpir el sitio o servidores o redes conectadas
          al sitio, o infringir cualquier requisito, procedimiento,
          pol&iacute;tica o normativa de las redes conectadas al sitio.
        </li>
        <li>recoger o almacenar datos personales sobre otros usuarios.</li>
      </ol>
      <strong>&nbsp;</strong>
      <p>Usted tambi&eacute;n est&aacute; de acuerdo en que:</p>
      <ol>
        <li>
          cualquier contenido generado por el usuario (como una edici&oacute;n
          virtual, anotaciones a una edici&oacute;n virtual y textos de
          escritura virtual) puede ser utilizado por otros en el contexto de la
          plataforma.
          <br />
          <em>
            Tenga en cuenta que la infraestructura est&aacute; diseñada para
            conservar la autor&iacute;a de todo el contenido de edici&oacute;n
            virtual y escritura virtual.
            <br />
          </em>{' '}
          Toda reproducci&oacute;n posterior, en cualquier medio, del contenido
          generado por usuarios del <em>Archivo LdoD</em> debe ser atribuida.
        </li>
        <li>
          todo el contenido generado por el usuario en el <em>Archivo LdoD</em>{' '}
          puede ser compartido de acuerdo con la licencia Creative Commons
          &ldquo;
          <a href="https://creativecommons.org/licenses/by-nc/4.0/">
            Attribution-NonCommercial 4.0 International
          </a>
          &rdquo; (CC BY-NC 4.0).{' '}
        </li>
        <li>
          las cuatro ediciones de los especialistas transcritas en el{' '}
          <em>Archivo LdoD</em> s&oacute;lo se pueden utilizar en el contexto de
          la plataforma, tal como se define en el{' '}
          <a href="https://ldod.uc.pt/about/copyright">Aviso de Copyright</a>.
        </li>
      </ol>
      <p>&nbsp;</p>
      <p>
        Los editores del <em>Archivo LdoD</em> se reservan el derecho de:
      </p>
      <ol>
        <li>
          eliminar cualquier contenido generado por el usuario que no cumpla con
          este c&oacute;digo de conducta.
        </li>
        <li>
          impedir a los usuarios registrados que no respeten este c&oacute;digo
          de conducta el acceso a las funcionalidades virtuales de
          edici&oacute;n y escritura de la plataforma.
        </li>
      </ol>
      <strong>&nbsp;</strong>
      <p>[actualizado 14-08-2017]</p>
    </div>
  );
};
