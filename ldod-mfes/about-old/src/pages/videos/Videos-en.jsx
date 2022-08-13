import { useEffect } from 'react';
export default ({ scroll, posY }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);

  return (
    <>
      <h1 className="text-center">Videos</h1>
      <p>&nbsp;</p>

      <p>
        This page contains videos with testimonials from two editors of the{' '}
        <em>Book of Disquiet</em> and the editor of the <em>LdoD Archive</em>.
        Teresa Sobral Cunha and Jer&oacute;nimo Pizarro were interviewed in
        November and December 2015, respectively. Manuel Portela was interviewed
        in August 2017. The interviews were edited according to topics related
        to both the material and editorial history of the{' '}
        <em>Book of Disquiet</em> and the nature of the
        <em>LdoD Archive</em> as a representation and simulation of the editions
        of the
        <em>Book</em>. In the case of the editors of the <em>Book</em>, the
        interviews focused on a set of questions about their first contact with
        the materials from the <em>Book</em>, their conceptualization of a model
        for the <em>Book</em>, their criteria for selecting and ordering its
        texts, and their perception about the current importance and
        significance of the <em>Book of Disquiet</em>. In the case of the editor
        of the <em>LdoD Archive</em>, the questions centered on the dynamic and
        simulatory nature of the Archive, and on its potential as a textual
        environment for learning, research and creativity. There is also a brief
        introduction to the <em>LdoD Archive</em>, showing excerpts from
        autographs and editions of two texts: &ldquo;Amo, pelas tardes demoradas
        de ver&atilde;o&rdquo; and &ldquo;Sinfonia de uma noite inquieta,&rdquo;
        which are read, respectively, by Jer&oacute;nimo Pizarro and Teresa
        Sobral Cunha. The topics of each video are referenced through
        intertitles, according to the sequence described below. Recording and
        editing of all videos: Tiago Cravid&atilde;o. Funding: Foundation for
        Science and Technology.
      </p>
      <br />

      <h5>Table of Contents:</h5>
      <ul>
        <li>
          <a onClick={(e) => scroll('#V1')}>Teaser_LdoD: LdoD Archive</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V2')}>
            Documentary reality is disorganized
          </a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V3')}>
            Vicente Guedes and Bernardo Soares
          </a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V4')}>Defining discursive units</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V5')}>Defining paragraphs</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V6')}>Human speech</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V7')}>Why editing?</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V8')}>Chronological organization</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V9')}>Heteronyms</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V10')}>Editing possibilities</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V11')}>Materiality and criticism</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V12')}>Spontaneous reading</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V13')}>Hyperconsciousness</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V14')}>LdoD Archive</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V15')}>Dynamic archive</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V16')}>Learning Researching Creating</a>
        </li>
        <li>
          <a onClick={(e) => scroll('#V17')}>
            Literary performativity simulator
          </a>
        </li>
      </ul>

      <br />
      <h5 id="V1">Teaser_LdoD: LdoD Archive</h5>

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

      <h5 id="V2">Documentary reality is disorganized</h5>
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

      <h5 id="V3">Vicente Guedes and Bernardo Soares</h5>
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

      <h5 id="V4">Defining discursive units</h5>
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

      <h5 id="V5">Defining paragraphs</h5>
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

      <h5 id="V6">Human speech</h5>
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

      <h5 id="V7">Why editing?</h5>
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

      <h5 id="V8">Chronological organization</h5>
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

      <h5 id="V9">Heteronyms</h5>
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

      <h5 id="V10">Editing possibilities</h5>
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

      <h5 id="V11">Materiality and criticism</h5>
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

      <h5 id="V12">Spontaneous reading</h5>
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

      <h5 id="V13">Hyperconsciousness</h5>
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

      <h5 id="V14">LdoD Archive</h5>
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

      <h5 id="V15">Dynamic archive</h5>
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

      <h5 id="V16">Learning Researching Creating</h5>
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

      <h5 id="V17">Literary performativity simulator</h5>
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

      <p>[updated 01-10-2017]</p>
    </>
  );
};
