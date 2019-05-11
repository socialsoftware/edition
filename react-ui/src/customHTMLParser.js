import React from 'react';
import ReactHTMLParser, { convertNodeToElement } from 'react-html-parser';
import { Link } from 'react-router-dom';


function transform(node) { // TODO: prevent render of head
    if (node.name === 'head' || node.name === 'nav') { return null; }

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

export default function customHTMLParser(html) {
    const options = { transform };
    return ReactHTMLParser(html, options)[1];
}
