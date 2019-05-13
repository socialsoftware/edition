import React from 'react';
import ReactHTMLParser, { convertNodeToElement } from 'react-html-parser';
import { Link } from 'react-router-dom';
import { Helmet } from 'react-helmet';


function transform(node) { // TODO: prevent tbody being placed as child of thead, see what is happening.
    if (node.name === 'head' || node.name === 'nav') { return null; }

    if (node.name === 'script') {
        return (
            <Helmet>
                <script type={node.attribs.type}>
                    {node.children[0].data}
                </script>
            </Helmet>
        );
    }

    if (node.name === 'a') {
        if (hasTrParent(node)) {
            console.log(node.attribs.href);
        }
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
        console.log(node);
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

function hasTrParent(node) {
    let aux = node;
    while (aux != null) {
        if (aux.parent && aux.parent.name === 'tr') { return true; }
        aux = aux.parent;
    }
    return false;
}

export default function customHTMLParser(html) {
    const options = { transform };
    console.log(html);
    return ReactHTMLParser(html, options)[1];
}
