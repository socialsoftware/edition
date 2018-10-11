import React, { Component } from 'react';
import { Grid, Row, Col, Panel, PanelGroup, Glyphicon} from 'react-bootstrap';
import { Link} from 'react-router-dom';
class About extends Component {
    
    render() {
        return (
            <Grid fluid>
			    <Row>
				    <Col md={12}>
                        <div className="text-center">
	    					<h1>Frequently Asked Questions <Glyphicon glyph="question-sign" /></h1>
					    </div>
				    </Col>
			    </Row>
			    <Row>				
                    <Col md={12}>
                        <PanelGroup accordion>
                            <Panel eventKey="1">
                                <Panel.Heading>
                                    <Panel.Title toggle>What is the <em>LdoD Archive</em>?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                <p>The <em>LdoD Archive</em> is a collaborative digital archive of the <em>Book of Disquiet</em> by Fernando Pessoa. It contains images of the autograph documents, new transcriptions of those documents and also
                                transcriptions of four editions of the work. 
                                <br></br>In addition to reading and comparing transcriptions, the <em>LdoD Archive</em> enables users to collaborate in creating virtual editions of the <em>Book of Disquiet</em>. It also includes a writing module which, in the future, will allow users to write variations based on fragments from the <em>Book</em>. 
                                Thus the <em>LdoD Archive</em> combines a representational principle with a simulation principle: the first is expressed through the representation of the history and processes of writing and editing the <em>Book</em>; the second is embodied in the fact that users are given the possibility of playing various roles in the literary process (reading, editing, writing), using the flexibility of the digital medium for experimenting with the
                                <em>Book of Disquiet</em> as a literary machine.</p>
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="2">
                                <Panel.Heading>
                                    <Panel.Title toggle>What is the the LdoD Classification Game?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                In the scope of a master's thesis developed at Instituto Superior Técnico that focuses in Crowdsourcing and Gamification in the Ldod Archive, the LdoD Classification game was created. 
                                <br></br>
                                The game makes use of the LdoD Archive capabilities, which allows users to create new editions of the Book of Disquiet and share them with the other users.
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="3">
                                <Panel.Heading>
                                    <Panel.Title toggle>What do I have to do to start playing the LdoD Classification Game?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                    In order to play the game you have firstly be a registered user of the <a href="https://ldod.uc.pt/signin"><em>LdoD Archive</em></a>, after that you can play in games in which you are a member of the Virtual Edition that is associated with the game or game that are open to every user of the <em>Archive</em> .
                                    Note that you also need to login on the game platform with your <em>LdoD Archive </em> credentials.
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="4">
                                <Panel.Heading>
                                    <Panel.Title toggle>How can I create a game for others to play?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                    You must be a Manager of a Virtual Edition on the <em>LdoD Archive</em>. From the management area of a Virtual Edition you can create games and their settings (open to all users/only members; date and time of the game; small description of the game and which fragment to use in the game).
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="5">
                                <Panel.Heading>
                                    <Panel.Title toggle>What is the goal of the game?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                    The overall objective of the game is to categorize fragments of Virtual Editions in a fun and entertaining way. The game is split into three main rounds explained bellow.
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="6">
                                <Panel.Heading>
                                    <Panel.Title toggle>What is Round 1 - Submit ?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                In this round, your goal is to read and analyse the paragraph shown on the screen in the given time and submit a tag which you think is adequate to classify what you read.  
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="7">
                                <Panel.Heading>
                                    <Panel.Title toggle>What is Round 2 - Vote?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                In this round, your goal is to vote in which of the tags suggested by the other game participants (and your own suggestion) is the best one. You can only vote once.
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="8">
                                <Panel.Heading>
                                    <Panel.Title toggle>What is Round 3 - Review?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                This is the last stage of the game, you will have the fragment in full in your screen and the tags that had the most votes in the earlier rounds and you must vote on the best one, however now you can change your vote will the timer is active. But be careful, changing votes constantly makes you lose points. Check the Score question below.
                                </Panel.Body>
                            </Panel>
                            <Panel eventKey="9">
                                <Panel.Heading>
                                    <Panel.Title toggle>How is my score determined?</Panel.Title>
                                </Panel.Heading>
                                <Panel.Body collapsible>
                                The score follows the following formula:
                                    Score = s + s-RWT + v-RWT + s-GWT + v-GWT + c, where
                                    <ul>
                                        <li>s, every submitted tag = 1 point</li>
                                        <li>s-RWT, you submitted a tag that won a round = 5 points </li>
                                        <li>v-RWT, you voted on a tag that won a round = 2 points </li>
                                        <li>s-GWT, you submitted the tag that won the <b>game</b> = 10 points </li>
                                        <li>v-GWT, you voted on the tag that won the <b>game</b> = 5 points </li>
                                        <li>c, for each vote change in the final round you <b>lose</b> 1 point </li>
                                    </ul>
                                </Panel.Body>
                            </Panel>
                        </PanelGroup>
                        <h4>For more information regarding the <em>LdoD Archive</em> check <a href="https://ldod.uc.pt/about/faq">here</a>. 
                        <br></br>
                        Other questions regarding the game, please contact us in the <Link to="/feedback">Feedback section</Link>.</h4>
			        </Col>
                </Row>
            </Grid>
        );
    }
}

export default About;