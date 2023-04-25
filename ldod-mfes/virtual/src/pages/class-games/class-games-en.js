/** @format */

export default /*html*/ `
        <h4 class="text-center mb">Classification Game</h4>
        <p>
            <a
                is="nav-to"
                href="${import.meta.env.VITE_NODE_HOST}/classification-game"
                target="_blank"
            >
                Click here to go to the Classification Game
        <span is="ldod-span-icon" icon="arrow-up-from-bracket" fill="#0d6efd" size="16px"></span>
            </a>
        </p>
        <p>
            Within the scope of a master's thesis developed at Instituto Superior Técnico focused on
            Crowdsourcing and Gamification in the
            <em>LdoD Archive</em>
            , the game
            <em>LdoD Classification</em>
            or LdoD Classification Game was created. The game uses virtual editions of the LdoD Archive, so
            registration is necessary to play and create games. The objective is to classify fragments of
            virtual editions through several games. Information required to play:
        </p>
        <ul>
            <li>Minimum of two players for a game to occur;</li>
            <li>Games occur at a specific hour and time defined at the time of creation;</li>
            <li>
                The duration of a game depends on the size of a fragment (however, a game does not take more
                than 5 to 10 minutes);
            </li>
            <li>
                The game consists of 3 rounds, and rounds 1 and 2 occur multiple times (one for each
                paragraph of the fragment):
                <ul>
                    <li>
                        Round 1 - Submit: The player has the first paragraph on the screen, reads and
                        analyzes it and then submits 1 and only 1 category that they consider appropriate;
                        (we move on to round 2 at the end of the time);
                    </li>
                    <li>
                        Round 2 - Vote: The time is now fixed at 15 seconds, the user has the same paragraph
                        that they saw in round 1 and also sees the categories submitted by all participants
                        and can only vote on one category that they consider appropriate (if there are no
                        more paragraphs to analyze, we move on to 3; otherwise, we move on to round 1, now
                        for the next paragraph);
                    </li>
                    <li>
                        Round 3 - Review: Time of 30 seconds, the user has the complete fragment on the
                        screen and the top voted categories so far. The user votes on a category, but can
                        change their vote while the time is available (changes in vote penalize their
                        points) and simultaneously sees the total votes, i.e., in real time, checking which
                        category is most voted, seeing the votes in the categories vary as the participants
                        vote. The objective is to determine the most appropriate category to classify the
                        fragment.
                    </li>
                    <li>
                        At the end of round 3, the player who suggested the winning category is credited as
                        its author in the virtual edition and corresponding fragment.
                    </li>
                </ul>
            </li>
        </ul>
        <p>Steps to create a game:</p>
        <ol>
            <li>
                It is necessary to be the manager of a virtual edition (if not, create a virtual edition),
                <strong>note that the virtual edition will have to have an open vocabulary</strong>
            </li>
            <li>
                The settings of the virtual edition condition the definitions of a game, that is: the game
                can be for all registered users or only members of the virtual edition, if the edition is
                private, only members of the virtual edition can play. If the edition is public, any of the
                cases may apply.
            </li>
            <li>
                Then go to Virtual -&gt; Virtual Editions -&gt; Manage (choose which edition you want to
                create the game) -&gt; Classification Game -&gt; Create;
            </li>
            <li>
                Fill in the parameters correctly and check the time, chosen fragment, and which players can
                play (Edition Members or Any Registered Users), click on Create.
            </li>
        </ol>
        <p>
            Then, to play, it's necessary to enter
            <a href="https://ldod.uc.pt/classification-game/" target="_blank">the game application</a>
            , log in (currently, registered users through a social network, such as Google or Facebook, are
            not supported) and, at the game time, click to start the game.
            <br />
            <strong>
                Finally, and in order to improve it by providing a better experience, please answer the
                survey. Your feedback is very valuable to us!
            </strong>
            The survey is available
            <a href="https://ldod.uc.pt/classification-game/feedback" target="_blank">here</a>
            , or go to the game page and click on the Feedback tab.
        </p>

`;
