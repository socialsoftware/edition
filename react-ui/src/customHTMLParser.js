import React from 'react';
import ReactHTMLParser, { convertNodeToElement } from 'react-html-parser';
import { Link } from 'react-router-dom';
import { Helmet } from 'react-helmet';


function transform(node) { // TODO: prevent tbody being placed as child of thead, see what is happening.
    if (node.name === 'nav' || node.name === 'link' || node.name === 'style'
          || node.name === 'meta' || node.name === 'title') { return null; }

    if (node.name === 'script' && node.attribs.src == null) {
        return (
            <Helmet>
                <script type="text/javascript">
                    {node.children[0].data}
                </script>
            </Helmet>
        );
    }

    if (node.type === 'tag' && node.name === 'a' && node.attribs.href && node.class !== 'infobutton') {
        if (node.attribs.href !== '#') {
            return (<Link
                to={node.attribs.href}>
                {node.children.map((child, index) => convertNodeToElement(child, index, transform))}
            </Link>);
        }
        // eslint disable because of eval
        /*eslint-disable */
        return (
            <a
                className={node.attribs.class}
                onClick={() => eval(`window.${node.attribs.onclick}()`)}>
                {node.children.map((child, index) => convertNodeToElement(child, index, transform))}
            </a>
        );
        /*eslint-enable */
    }
    return undefined;
}

/* function hasTdParent(node) {
    let aux = node;
    while (aux != null) {
        if (aux.parent && aux.parent.name === 'td') { return true; }
        aux = aux.parent;
    }
    return false;
} */

export default function customHTMLParser(html) {
    const options = { transform };
    console.log(html);
    return ReactHTMLParser(html, options)[1];
}
