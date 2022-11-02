import { highlightText } from '@apache-annotator/dom';
import { fromRange, toRange } from 'xpath-range';

export const highlightAnnotation = ({ ranges: [range] }, node) => {
  const r = toRange(
    `/${range.start}`,
    range.startOffset,
    `/${range.end}`,
    range.endOffset,
    node.querySelector('#virtual-transcriptionWrapper')
  );
  highlightText(r);
};

/* 
 RangeJSON contract : 
{ 
  start: "/div[x:number]/div[y:number]/p...",
  end: "/div[x:number]/div[y:number]/p...",
  startOffset: x:number,
  endOffset: x:number,
}

#1 To highlight annotations

 case 1 - start === end

1 - Obter innerHTML of the element through CSS selectors: querySelector :nth-child()
2 - apply slice function according start and end offset to split the innerHTML in three (save values in variables)
3 - Wrap the middle section on a span element with highlight style
4 - Remove innerHTML of the element
5 - Append elements to element: the first as new Text, middle as span and last as New Text; 


case 2 - start !== end

example:
  end: "/div[1]/div[1]/p[7]"
  endOffset: 109
  start: "/div[1]/div[1]/p[5]"
  startOffset :531



*/
