import type { NextApiRequest, NextApiResponse } from 'next'
// import TelegramBot from 'node-telegram-bot-api';
 
type ResponseData = {
  message: any
}

export default function handler(
  req: NextApiRequest,
  res: NextApiResponse<ResponseData>
) {
  // bot.sendMessage(1723345563, `${{message: req.query}}`).then((msg)=> console.warn(msg)).catch((err)=> console.error(err));
  res.status(200).json({message: req.query})
}