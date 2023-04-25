/** @format */

export default /*html*/ `
        <h4 class="text-center mb">Juego de Clasificación</h4>
        <p>
            <a
                is="nav-to"
                href="${import.meta.env.VITE_NODE_HOST}/classification-game"
                target="_blank"
            >Clic aquí para visitar el Juego de Clasificación
        <span is="ldod-span-icon" icon="arrow-up-from-bracket" fill="#0d6efd" size="16px"></span></a>
        </p>

        <p>
            En el marco de una tesis de maestría desarrollada en el Instituto Superior Técnico centrada en
            Crowdsourcing y Gamificación en el
            <em>Archivo LdoD</em>
            , se creó el juego
            <em>LdoD Classification</em>
            o Juego de Clasificación de LdoD. El juego hace uso de las ediciones virtuales del Archivo LdoD,
            por lo que es necesario estar registrado para poder jugar y crear juegos. El objetivo es
            clasificar fragmentos de ediciones virtuales a través de varios juegos. Información necesaria
            para jugar:
        </p>
        <ul>
            <li>Se requieren al menos dos jugadores para que se lleve a cabo un juego;</li>
            <li>
                Los juegos ocurren en una hora y tiempo específico definido en el momento de su creación;
            </li>
            <li>
                La duración de un juego depende del tamaño de un fragmento (sin embargo, un juego no dura
                más de 5 a 10 minutos);
            </li>
            <li>
                El juego consta de 3 rondas, y las rondas 1 y 2 ocurren varias veces (una para cada párrafo
                del fragmento):
                <ul>
                    <li>
                        Ronda 1 - Enviar: El jugador tiene en pantalla el primer párrafo, lo lee y analiza y
                        luego envía una y solo una categoría que considera adecuada; (avanzamos a la ronda 2
                        al final del tiempo);
                    </li>
                    <li>
                        Ronda 2 - Votar: El tiempo ahora es fijo de 15 segundos, el usuario tiene de nuevo
                        el mismo párrafo que vio en la ronda 1 y también ve las categorías enviadas por
                        todos los participantes y solo puede votar por una categoría que considere adecuada
                        (si no hay más párrafos para analizar, avanzamos a la ronda 3; de lo contrario,
                        avanzamos a la ronda 1, ahora para el siguiente párrafo);
                    </li>
                    <li>
                        Ronda 3 - Revisar: Tiempo de 30 segundos, el usuario tiene en pantalla el fragmento
                        completo y en la parte superior las categorías más votadas hasta el momento. El
                        usuario vota por una categoría, pero puede cambiar su voto mientras el tiempo esté
                        disponible (siendo que los cambios de voto penalizan sus puntos) y simultáneamente
                        ve los votos totales, es decir, está verificando en tiempo real cuál es la categoría
                        más votada, viendo los votos en las categorías que van variando según los
                        participantes van votando. El objetivo es determinar la categoría más adecuada para
                        clasificar el fragmento.
                    </li>
                    <li>
                        Terminada la ronda 3, el jugador que sugirió la categoría ganadora es acreditado
                        como el autor de la misma en la edición virtual y fragmento correspondiente.
                    </li>
                </ul>
            </li>
        </ul>
        <p>Pasos para crear un juego:</p>
        <ol>
            <li>
                Es necesario ser gestor de una edición virtual (en caso de no serlo, cree una edición
                virtual),
                <strong>tenga en cuenta que la edición virtual debe tener un vocabulario abierto</strong>
                .
            </li>
            <li>
                La configuración de la edición virtual condiciona las definiciones de un juego, es decir, el
                juego puede ser para todos los usuarios registrados o solo para miembros de la edición
                virtual. Si la edición es privada, solo los miembros de la edición virtual pueden jugar. Si
                la edición es pública, cualquiera puede jugar.
            </li>
            <li>
                Vaya a Virtual -> Ediciones Virtuales -> Gestión (elegir la edición en la que desea crear el
                juego) -> Juego de Clasificación -> Crear.
            </li>
            <li>
                Complete los parámetros adecuadamente y verifique la hora, el fragmento elegido y quiénes
                son los jugadores que pueden jugar (miembros de la edición o cualquier usuario registrado),
                haga clic en Crear.
            </li>
        </ol>
        <p>
            Luego, para jugar, es necesario ingresar a
            <a href="https://ldod.uc.pt/classification-game/" target="_blank">la aplicación del juego</a>
            , iniciar sesión (actualmente no se admiten usuarios registrados a través de una red social,
            como Google o Facebook) y, a la hora del juego, hacer clic en comenzar el juego.
            <br />
            <strong>
                Finalmente, para mejorar el juego y proporcionar una mejor experiencia, responda la
                encuesta. ¡Sus comentarios son muy valiosos para nosotros!
            </strong>
            La encuesta está disponible
            <a href="https://ldod.uc.pt/classification-game/feedback" target="_blank">aquí</a>
            , o vaya a la página del juego y haga clic en la pestaña de comentarios.
        </p>

`;
