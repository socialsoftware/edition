import { useEffect } from 'react';

export default ({ posY, scroll }) => {
  useEffect(() => window.scrollTo({ top: posY }), []);
  return (
    <>
      <h1 className="text-center">Text Encoding</h1>
      <p>&nbsp;</p>
      <h3>1. The XML TEI guidelines</h3>
      <p>
        All transcriptions in the <em>LdoD Archive</em> have been encoded
        according to the{' '}
        <a href="http://www.tei-c.org/Guidelines/" target="new">
          <em>TEI Guidelines</em>
        </a>{' '}
        (Text Encoding and Interchange). These guidelines for text encoding have
        been developed since 1988 by a consortium of institutions — the{' '}
        <a href="http://www.tei-c.org/index.xml" target="new">
          Text Encoding Initiative Consortium
        </a>
        . The TEI guidelines define a large set of elements and attributes in
        XML (
        <a href="https://en.wikipedia.org/wiki/XML" target="new">
          Extensible Markup Language
        </a>
        ) for representing structural, conceptual, and rendering features of
        texts. Considering that the XML format is both a human and
        machine-readable text encoding language, TEI can be described as a
        textual analysis method for digital processing. It takes advantage of
        the interoperability of the XML format, in order to describe in detail
        textual facts and events through a specific syntax and semantics. It is
        a very powerful convention, composed of encoding modules for all kinds
        of problems of representation of structures and textual forms, which
        extend from the invention of writing to current electronic documents.
        The current version of the{' '}
        <em>Guidelines for Electronic Text Encoding and Interchange</em> (TEI
        P5) was published in November 2007 and continues to be updated
        periodically. At the time of writing, the latest update was made in July
        2017 (version 3.2.0).
      </p>
      <p>&nbsp;</p>
      <h3>
        2. XML TEI applied to the <em>Book of Disquiet</em>
      </h3>
      <p>
        In the case of the <em>LdoD Archive</em>, our TEI encoding schema
        involved the parallel segmentation method and the definition of a
        critical apparatus which treats all transcriptions as textual
        variations. Thus, one can automatically compare not only variations that
        occur in the witnesses, but also variations between witness
        transcriptions across the editions. Although witnesses are marked up as
        such, all variations (whether they occur in the autographs or in the
        editions) are processed at the same level. This encoding decision
        expresses the theoretical principle that underlies the
        <em> LdoD Archive: </em>that of showing the <em>Book of Disquiet</em> as
        both authorial project and editorial project, making it possible to
        observe the transformation of the archive into editions as a dynamic
        process of generation of textual variations.
      </p>
      <p>
        Our XML TEI encoding template embodies that which in our virtualization
        model for the <em>Book of Disquiet</em> we describe as the genetic
        dimension, that is, the process of authoring (writing and rewriting),
        and also that which we have called the social dimension, that is, the
        historical process of reinscription and socialization of texts (editing
        and reediting) under specific editorial and bibliographic forms (Portela
        &amp; Silva, 2014). In turn, the virtual dimension (which is the
        designation of textual socialization of the <em>Book of Disquiet</em> in
        the specific context of the <em>LdoD Archive</em>) consists of the
        possibility of constructing virtual editions through the combination of
        textual units as defined by the authorial interpretations and/or
        editorial interpretations (thus inheriting the XML TEI structure of
        those units). The dynamism of the <em>LdoD Archive</em> depends on the
        modularization produced by its data model, but also on the versatility
        in comparing transcriptions enabled by its text encoding template.
      </p>
      <p>
        This way, XML TEI encoding is used simultaneously in its
        representational affordance — that is, in terms of a granular
        description of specific events on the inscription surface — and in its
        relational affordance — that is, in terms of the instantaneous reading
        of micro and macrovariations at different textual scales within the
        corpus. It is as if all transcriptions of all witnesses and editions
        were part of a single tree, making words, punctuation marks, blank
        spaces, paragraph divisions, and text divisions comparable to each
        other. The exhaustiveness and complexity of the encoding of the
        variations allows us to move recursively across three scales: the scale
        of character, punctuation mark, and blank space as minimum units of
        inscription; the scale of word, sentence, paragraph and textual block as
        semantic units; and the scale of ordered sequence of texts and book as
        discursive and material instantiations of a certain idea of the work.
      </p>
      <p>&nbsp;</p>
      <h3>3. Encoding criteria</h3>
      <p>
        {' '}
        The <em>LdoD Archive </em>team has adopted a set of criteria for
        encoding the various acts of authorial and editorial inscription. These
        criteria were sometimes determined by the need to simplify the
        processing and visualization of marked-up text. This section explains
        the main options taken to ensure the balance between the textual and
        semantic accuracy of the markup, on the one hand, and the readability of
        the visualization of the genetic layer and of the comparisons across the
        transcriptions, on the other. Although there is a mimetic topographic
        component in the markup of the witnesses, it is subordinated to the
        abstract data model whose main function is to guarantee the
        comparability both across the transcriptions and across the relative
        order of the documents in each edition. Our main goal is not to emulate
        the documents but to model them for processing purposes.
      </p>
      <p>
        <strong>Encoding spaces and division markers</strong>
        <br />
        Spaces between paragraphs, between title and text, and between blocks of
        text have been marked up<em> in their relative proportions</em>, both in
        relation to the witnesses and in relation to the editions. Instead of
        first-line indentation for paragraphs, we opted for one-line spacing
        between paragraphs, in order to improve the readability of the
        transcriptions for screen reading. All other spaces correspond to spaces
        that appear in the witnesses and editions. All division markers also
        correspond to dashes and horizontal rulers that appear in the witnesses
        and editions. Spaces and division markers are important elements in the
        articulation of the internal and external divisions of each text.
      </p>
      <p>
        <strong>Encoding deletions</strong>
        <br />
        Deleted text (&lt;del&gt;) has been marked up with the values
        “overstrike” and “overtyped”. Both are displayed by a strike over the
        erased word.
      </p>
      <p>
        <strong>Encoding additions</strong>
        <br />
        Additions (&lt;add&gt;) have been marked up with the values “below”,
        “above”, “inline”, “top”, “bottom” and “margin,” which define the place
        (&lt;place&gt;) of the added text in relation to the text segment to
        which it applies or the place where it appears on the page. The “margin”
        value was usually reserved for added text in the margins, but{' '}
        <em>not for text that simply continues on the margins</em>, as often
        happens. In these cases, the change of place of the text on the page is
        marked up only by a line break or a line break and a space.
      </p>
      <p>
        <strong>Encoding substitutions </strong>
        <br />
        Encoding substitutions (&lt;subst&gt;) involves marking up a deleted
        element (&lt;del&gt;) and an added element (&lt;add&gt;). The simple
        visualization of the witness transcription can sometimes suggest that a
        given element is an addition, since it is preceded by the sign ∧,
        generally used as a sign for inserted elements. In such cases, the
        ambiguity can be clarified by selecting the “show deleted”, “highlight
        inserted” and “highlight substitutions” boxes at the same time. In the
        second paragraph of{' '}
        <a
          href="https://ldod.uc.pt/fragments/fragment/Fr044/inter/Fr044_WIT_MS_Fr044a_51"
          target="new">
          BNP/E3 1-50r
        </a>
        , for example, the interface shows an addition (dean- teira ∧d’ella)
        when the witness has only the deletion of the plural ending (dean- teira
        d’ellas). In this case, the option of marking up the substitution
        implied explicitly replacing “d’ellas” by “d’ella” (as if there was, in
        fact, an addition), since that is the semantic implication of the
        deletion. This marking up rule{' '}
        <em>
          has been applied to all occurrences where partial deletion of a word
          transforms it into another word — for example, a plural into a
          singular or a masculine into a feminine.{' '}
        </em>
        Although there is no addition, the change is graphically and
        semantically marked up as if there was. The disambiguation of these
        encoding options implies that the boxes “show deleted”, “highlight
        inserted” and “highlight substitutions” are checked, as well as the
        collation with the digital facsimile. This option was taken because of
        textual rendering, since the marking up of isolated letters would result
        in the presentation of a space before and after the letter, decreasing
        readability. The “show deleted” and “highlight inserted” options were
        not synchronized (which would have helped clarify that ambiguity) since
        their separate use allows users to observe revision sites more clearly.
      </p>
      <p>
        <strong>Encoding variants</strong>
        <br />
        The marking up of variants is rendered through the simultaneous presence
        of the alternatives in a different color from the rest of the text.
        Besides, when you mouse over each variant, the associated probability
        value appears. This value results from the division of the unit by the
        number of textual variants. In the first paragraph of{' '}
        <a
          href="https://ldod.uc.pt/fragments/fragment/Fr044/inter/Fr044_WIT_MS_Fr044a_51"
          target="new">
          BNP/E3 1-50r
        </a>
        , for example, the interface shows the variants “rancor” and “torpor”
        with a probability of being excluded of 0.5 each (“excl 0.5”). When
        there are three variants, the distribution is usually a combination of
        0.3, 0.3 and 0.4. However, there are cases in which the need to combine
        topographical markup with variant markup{' '}
        <em>
          implied the use of more values than the number of actual textual
          variants
        </em>
        , since line breaks cannot be marked up inside the probability values
        and it is necessary to retag the variant as a variant after each line
        break. In these cases, the value of the probability is arbitrary and
        must be adjusted to reflect the real number of variants, and not the
        number of line breaks, as it happens. It is a situation that arises from
        the impossibility of nesting those two hierarchies in the XML TEI
        syntax.
      </p>
      <p>
        <strong>Encoding variations</strong>
        <br />
        The adopted encoding also enables the observation of all variations
        across the interpretations, that is, it allows us to compare our new
        transcription of the witnesses against the transcription in Prado
        Coelho-1982 against the transcription in Sobral Cunha-2008 against the
        transcription in Zenith-2012 against the transcription in Pizarro-2010
        for each fragment of the <em>Book of Disquiet</em>. The critical
        apparatus (&lt;app&gt;) of these variations was classified according to
        five typologies (&lt;type&gt;): “orthographic”, which identifies
        orthographic variations; “punctuation”, for punctuation variations;
        “paragraph”, which marks up variations in paragraph division; “style”
        for identifying typographic style variations (for example, use of
        italics or capitals); and “substantive” to indicate differences in the
        transcribed word (for example, when experts select divergent variants or
        read a particular passage differently). Marked-up variations (which
        refer to the existing differences{' '}
        <em>in the set of all the interpretations of a given fragment</em>) are
        visualized either through colour highlighting or through the table of
        variations for each comparison.
      </p>
      <p>&nbsp;</p>
      <h3>4. Additional information</h3>
      Anyone interested in a more technical description of our encoding options
      can look at the template of an annotated XML file{' '}
      <strong>
        <a href="/encoding/LdoD_XML-Template_EN.xml" target="new">
          HERE
        </a>
      </strong>
      . We also suggest the reading of the following article: António Rito Silva
      and Manuel Portela, “
      <a href="http://jtei.revues.org/1171" target="new">
        TEI4LdoD: Textual Encoding and Social Editing in Web 2.0 Environments.
      </a>
      ” <em>Journal of the Text Encoding Initiative</em> 8 (2014-2015).<p></p>
      <p>&nbsp;</p>
      <p>[updated 08-09-2017]</p>
    </>
  );
};
