import React from 'react';
import ReactHTMLParser, { convertNodeToElement } from 'react-html-parser';
import { Link } from 'react-router-dom';
import Helmet from 'react-helmet';


function transform(node) {
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
    if (node.name === 'script') {
        if (node.attribs.src) {
            return (
                <Helmet>
                    <script src={node.attribs.src} />
                </Helmet>
            );
        }
        return (
            <Helmet>
                <script type={node.attribs.type}>
                    {node.children[0].data}
                </script>
            </Helmet>
        );
    }
    if (node.name === 'link') {
        return (
            <Helmet>
                <link rel={node.attribs.rel} type={node.attribs.type} href={node.attribs.href} />
            </Helmet>
        );
    }
    return undefined;
}

export default function customHTMLParser(html) {
    const options = { transform };
    return ReactHTMLParser(html, options)[0];
}
