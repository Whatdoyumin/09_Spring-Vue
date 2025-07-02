import api from '@/api';

const BASE_URL = '/api/board';

export default {
  async getList(params) {
    const { data } = await api.get(BASE_URL, { params });
    console.log('BOARD GET LIST: ', data);

    return data;
  },
};
